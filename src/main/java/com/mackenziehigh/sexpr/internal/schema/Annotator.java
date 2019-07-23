package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Schema.Builder;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.annotations.After;
import com.mackenziehigh.sexpr.annotations.Before;
import com.mackenziehigh.sexpr.annotations.Condition;
import com.mackenziehigh.sexpr.annotations.Pass;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author mackenzie
 */
public class Annotator
{
    private final Schema.Builder builder;

    public Annotator (final Builder builder)
    {
        this.builder = builder;
    }

    /**
     * Given an object containing properly annotated methods,
     * define the conditions and actions defined therein.
     *
     * @param object contains condition and action definitions.
     */
    public void defineViaReflection (final Object object)
    {
        final Optional<String> defaultPass = getDefaultPass(object);

        for (Method method : object.getClass().getMethods())
        {
            if (method.isAnnotationPresent(Condition.class))
            {
                defineConditionViaReflection(object, method);
            }

            if (method.isAnnotationPresent(Before.class))
            {
                defineBeforeActionByReflection(defaultPass, object, method);
            }

            if (method.isAnnotationPresent(After.class))
            {
                defineAfterActionByReflection(defaultPass, object, method);
            }
        }
    }

    private Optional<String> getDefaultPass (final Object object)
    {
        if (object.getClass().isAnnotationPresent(Pass.class))
        {
            final String name = object.getClass().getAnnotation(Pass.class).value();
            return Optional.of(name);
        }
        else
        {
            return Optional.empty();
        }
    }

    private String getPass (final Optional<String> defaultPass,
                            final Method method)
    {
        if (method.isAnnotationPresent(Pass.class))
        {
            final String name = method.getAnnotation(Pass.class).value();
            return name;
        }
        else if (defaultPass.isPresent())
        {
            return defaultPass.get();
        }
        else
        {
            throw new IllegalArgumentException("No translation pass was specified on either the class or method.");
        }
    }

    private void defineConditionViaReflection (final Object object,
                                               final Method method)
    {
        /**
         * Obtain the user-defined name of the condition.
         */
        final String name = method.getAnnotation(Condition.class).value();

        /**
         * The method cannot throw any checked exceptions.
         */
        if (method.getExceptionTypes().length != 0)
        {
            final String message = String.format("Do *not* throw checked exceptions in (%s).", method.toString());
            throw new IllegalArgumentException(message);
        }

        /**
         * The return-type of the method must be boolean.
         */
        if (method.getReturnType().equals(boolean.class) == false)
        {
            final String message = String.format("You must return boolean from (%s), not %s.",
                                                 method.toString(),
                                                 method.getReturnType().getName());
            throw new IllegalArgumentException(message);
        }

        /**
         * The method must take exactly one argument,
         * which must be a symbolic-expression.
         */
        if (method.getParameterCount() != 1)
        {
            final String message = String.format("Method (%s) must take exactly one parameter.", method.toString());
            throw new IllegalArgumentException(message);
        }
        else if (method.getParameterTypes()[0].equals(Sexpr.class))
        {
            final Function<Sexpr, Object> invocation = createInvocation(object, method);
            final Predicate<Sexpr> condition = x -> (Boolean) invocation.apply(x);
            builder.condition(name, condition);
        }
        else if (method.getParameterTypes()[0].equals(SAtom.class))
        {
            final Function<SAtom, Object> invocation = createInvocation(object, method);
            final Predicate<Sexpr> condition = x -> x.isAtom() ? (Boolean) invocation.apply(x.asAtom()) : false;
            builder.condition(name, condition);
        }
        else if (method.getParameterTypes()[0].equals(SList.class))
        {
            final Function<SList, Object> invocation = createInvocation(object, method);
            final Predicate<Sexpr> condition = x -> x.isList() ? (Boolean) invocation.apply(x.asList()) : false;
            builder.condition(name, condition);
        }
        else
        {
            final String message = String.format("Method (%s) must take a %s|%s|%s as its only parameter.",
                                                 method.toString(),
                                                 Sexpr.class.getName(),
                                                 SAtom.class.getName(),
                                                 SList.class.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void defineBeforeActionByReflection (final Optional<String> defaultPass,
                                                 final Object object,
                                                 final Method method)
    {
        /**
         * Obtain the name of the translation pass that this action applies to.
         */
        final String pass = getPass(defaultPass, method);

        /**
         * Obtain the user-defined name of the rule that this action applies to.
         */
        final String rule = method.getAnnotation(Before.class).value();

        /**
         * The method cannot throw any checked exceptions.
         */
        if (method.getExceptionTypes().length != 0)
        {
            final String message = String.format("Do *not* throw checked exceptions in (%s).", method.toString());
            throw new IllegalArgumentException(message);
        }

        /**
         * The return-type of the method must be void.
         */
        if (method.getReturnType().equals(void.class) == false)
        {
            final String message = String.format("You must return boolean from (%s), not %s.",
                                                 method.toString(),
                                                 method.getReturnType().getName());
            throw new IllegalArgumentException(message);
        }

        /**
         * The method must take exactly one argument,
         * which must be a symbolic-expression.
         */
        if (method.getParameterCount() != 1)
        {
            final String message = String.format("Method (%s) must take exactly one parameter.", method.toString());
            throw new IllegalArgumentException(message);
        }
        else if (method.getParameterTypes()[0].equals(Sexpr.class))
        {
            final Function<Sexpr, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x);
            builder.before(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SAtom.class))
        {
            final Function<SAtom, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.asAtom());
            builder.before(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SList.class))
        {
            final Function<SList, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.asList());
            builder.before(pass, rule, action);
        }
        else
        {
            final String message = String.format("Method (%s) must take a %s|%s|%s as its only parameter.",
                                                 method.toString(),
                                                 Sexpr.class.getName(),
                                                 SAtom.class.getName(),
                                                 SList.class.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void defineAfterActionByReflection (final Optional<String> defaultPass,
                                                final Object object,
                                                final Method method)
    {
        /**
         * Obtain the name of the translation pass that this action applies to.
         */
        final String pass = getPass(defaultPass, method);

        /**
         * Obtain the user-defined name of the rule that this action applies to.
         */
        final String rule = method.getAnnotation(After.class).value();

        /**
         * The method cannot throw any checked exceptions.
         */
        if (method.getExceptionTypes().length != 0)
        {
            final String message = String.format("Do *not* throw checked exceptions in (%s).", method.toString());
            throw new IllegalArgumentException(message);
        }

        /**
         * The return-type of the method must be void.
         */
        if (method.getReturnType().equals(void.class) == false)
        {
            final String message = String.format("You must return boolean from (%s), not %s.",
                                                 method.toString(),
                                                 method.getReturnType().getName());
            throw new IllegalArgumentException(message);
        }

        /**
         * The method must take exactly one argument,
         * which must be a symbolic-expression.
         */
        if (method.getParameterCount() != 1)
        {
            final String message = String.format("Method (%s) must take exactly one parameter.", method.toString());
            throw new IllegalArgumentException(message);
        }
        else if (method.getParameterTypes()[0].equals(Sexpr.class))
        {
            final Function<Sexpr, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x);
            builder.after(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SAtom.class))
        {
            final Function<SAtom, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.asAtom());
            builder.after(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SList.class))
        {
            final Function<SList, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.asList());
            builder.after(pass, rule, action);
        }
        else
        {
            final String message = String.format("Method (%s) must take a %s|%s|%s as its only parameter.",
                                                 method.toString(),
                                                 Sexpr.class.getName(),
                                                 SAtom.class.getName(),
                                                 SList.class.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private <T> Function<T, Object> createInvocation (final Object object,
                                                      final Method method)
    {
        final Function<T, Object> function = x ->
        {
            try
            {
                method.setAccessible(true);
                return method.invoke(object, x);
            }
            catch (Throwable ex)
            {
                throw new RuntimeException(x.toString(), ex);
            }
        };

        return function;
    }
}
