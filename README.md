# Sexpr
An S-expression library, for Java, focused on creating Domain Specific Languages.  

*This library is still undergoing alpha development. The API may evolve without notice.*

**JavaDoc:** https://www.mackenziehigh.com/shadow/public/Sexpr/branches/master/target/site/apidocs/index.html

**Coverage:** https://www.mackenziehigh.com/shadow/public/Sexpr/branches/master/target/site/jacoco/index.html

## Symbolic Expressions

### Symbolic Atoms

A Symbolic Atom consists of a series of characters or a string literal. 

A Symbolic Atom is a leaf in a Symbolic Expression tree data-structure. 


### Symbolic Lists

A Symbolic List consists of a series of zero-or-more Symbolic Atoms or nested Symbolic Lists. 

A Symbolic List is an interior node in a Symbolic Expression tree data-structure. 


## Schemas 

A Schema is a pattern that describes a Symbolic Expression tree data-structure. Schemas are closely related to formal grammars used by parser generators. In particular, schemas are based on Parsing Expression Grammars conceptually. As such, a schema consists of a set of rules that can be used to *match* an expression. 


### Hello World Schema

```java
package examples;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Schema.Match;

public final class Example
{
    public static void main (String[] args)
    {
        // Create a Schema that describes exactly four Symbolic Lists, namely:
        // 1. (Hello Earth)
        // 2. (Hello Mars)
        // 3. (Goodbye Earth)
        // 4. (Goodbye Mars)
        final Schema schema = Schema.newBuilder()
                .include("(root = (seq (ref verb) (ref planet)))")
                .include("(verb = (either (word Hello) (word Goodbye)))")
                .include("(planet = (either (word Earth) (word Mars)))")
                .build();

        // Create a Symbolic List containing two Symbolic Atoms. 
        // Notice that the enclosing parentheses are implicit. 
        final SList input = SList.parse("Hello Earth");
        System.out.println("Input = " + input);

        // Determine whether the Symbolic List obeys the afore Schema. 
        final Match result = schema.match(input);
        System.out.println("Match? = " + result.isSuccess());
    }
}
```

**Output:**

```bash
Input = (Hello Earth)
Match? = true
```

### Rules

A schema consists of a set of rules, which each describe a pattern. The rules can be composed to form more complex patterns. Further, rules can be assigned names, and then used within other rules, in order to form recursively defined patturns. 

#### Root Rules

Since a schema consists of a set of rules, one of them must be identified as the *root* rule. The *root* rule will be the first to be applied to a given input. There are two ways to specify the *root* rule. Firstly, the *root* rule can be specified explicitly using a *root declaration*. Secondly, any rule may be implicitly defined as the *root* rule by naming that rulle 'root'. Only one rule can be the root. 

**Syntax of Explicit Root Declaration:**

```bash
(root <name>)
```

Here `<name>` is the name of the rule that is the *root* rule. 

**Example of Explicit Root Declaration:**

```java
package examples;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Schema.Match;

public final class Example
{
    public static void main (String[] args)
    {
        // Create a Schema that describes exactly four Symbolic Lists, namely:
        // 1. (Hello Earth)
        // 2. (Hello Mars)
        // 3. (Goodbye Earth)
        // 4. (Goodbye Mars)
        final Schema schema = Schema.newBuilder()
                .include("(root sentence)") // EXPLICIT ROOT DECLARATION
                .include("(sentence = (seq (ref verb) (ref planet)))")
                .include("(verb = (either (word Hello) (word Goodbye)))")
                .include("(planet = (either (word Earth) (word Mars)))")
                .build();

        // Create a Symbolic List containing two Symbolic Atoms.
        // Notice that the enclosing parentheses are implicit.
        final SList input = SList.parse("Hello Earth");
        System.out.println("Input = " + input);

        // Determine whether the Symbolic List obeys the afore Schema.
        final Match result = schema.match(input);
        System.out.println("Match? = " + result.isSuccess());
    }
}
```

**Syntax of Implicit Root Declaration:**

```bash
(root = <rule>)
```
 
**Example of Implicit Root Declaration:**

```java
package examples;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Schema.Match;

public final class Example
{
    public static void main (String[] args)
    {
        // Create a Schema that describes exactly four Symbolic Lists, namely:
        // 1. (Hello Earth)
        // 2. (Hello Mars)
        // 3. (Goodbye Earth)
        // 4. (Goodbye Mars)
        final Schema schema = Schema.newBuilder()
                .include("(sentence = (seq (ref verb) (ref planet)))") // IMPLICIT ROOT DECLARATION
                .include("(verb = (either (word Hello) (word Goodbye)))")
                .include("(planet = (either (word Earth) (word Mars)))")
                .build();

        // Create a Symbolic List containing two Symbolic Atoms.
        // Notice that the enclosing parentheses are implicit.
        final SList input = SList.parse("Hello Earth");
        System.out.println("Input = " + input);

        // Determine whether the Symbolic List obeys the afore Schema.
        final Match result = schema.match(input);
        System.out.println("Match? = " + result.isSuccess());
    }
}
```

#### Atom Rules

An *Atom Rule* describes a symbolic-atom using a regular-expression. 

**Syntax of an Atom Rule:**

```bash
(atom <regex>)
```

Here ```<regex>``` is a standard [Java-based regular-expression](https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/regex/Pattern.html).

**Example of an Atom Rule:**

```
package examples;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.Schema;

public final class Example
{
    public static void main (String[] args)
    {
        // Create a Schema that describes Symbolic Atoms,
        // such that the textual representations of the atoms
        // are sequences of 1s and 0s (binary text).
        final Schema schema = Schema.newBuilder()
                .include("(root = (atom '[01]+'))") // HERE IS AN ATOM RULE.
                .build();

        // Create some example atoms.
        final var atom1 = SAtom.fromString("10001");
        final var atom2 = SAtom.fromString("10101");
        final var atom3 = SAtom.fromString("12345");

        // Determine whether the atoms obey the Schema.
        System.out.println("Atom #1 = " + schema.match(atom1).isSuccess());
        System.out.println("Atom #2 = " + schema.match(atom2).isSuccess());
        System.out.println("Atom #3 = " + schema.match(atom3).isSuccess());
    }
}
```

**Output:**

```bash
Atom #1 = true
Atom #2 = true
Atom #3 = false
```


#### Word Rules

A *Word Rule* is quite similar to an *Atom Rule*, except a literal string is used, instead of a regular-expression. 

**Syntax of an Word Rule:**

```bash
(word <string>)
```

Here ```<string>``` is some literal text.


**Example of an Word Rule:**

```
package examples;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.Schema;

public final class Example
{
    public static void main (String[] args)
    {
        // Create a Schema that describes Symbolic Atoms,
        // such that the textual representations of the atoms
        // are sequences of 1s and 0s (binary text).
        final Schema schema = Schema.newBuilder()
                .include("(root = (word 'Earth'))") // HERE IS A WORD RULE.
                .build();

        // Create some example atoms.
        final var atom1 = SAtom.fromString("Venus");
        final var atom2 = SAtom.fromString("Earth");
        final var atom3 = SAtom.fromString("Mars");

        // Determine whether the atoms obey the Schema.
        System.out.println("Atom #1 = " + schema.match(atom1).isSuccess());
        System.out.println("Atom #2 = " + schema.match(atom2).isSuccess());
        System.out.println("Atom #3 = " + schema.match(atom3).isSuccess());
    }
}
```

**Output:**

```bash
Atom #1 = false
Atom #2 = true
Atom #3 = false
```

#### Sequence Rules

A *Sequence Rule* describes a *Symbolic List* using a series of nested rules and/or repetitions of nested rules. 


**Syntax of a Sequence Rule:**

```bash
(seq <element-0> ... <element-N>)
```

Here each *element* is either an *option quantifier*, *star quantifier*, *plus  quantifier*, *repeat quantifier*, or a nested rule. An *option quantifier* means that a nested rule may be repeated zero-or-one times. A *star quantifier* means that a nested rule may be repeated zero-or-more times. A *plus quantifier* means that a nested rule may be repeated one-or-more times. A *repeat quantifier* explicitly specifies the minimum and maximum number of times that a nested rule may be repeated. 

**Syntax of an Option Quantifier:**

```bash
(option <rule>)
```

**Syntax of an Star Quantifier:**

```bash
(star <rule>)
```

**Syntax of an Plus Quantifier:**

```bash
(plus <rule>)
```

**Syntax of an Repeat Quantifier:**

```bash
(repeat <rule> <minimum> <maximum>)
```

**Example Sequence Rules:**

| Example Rule                                  | Example Input | Matches? |
|-----------------------------------------------|---------------|----------|
| (seq (word A) (word B) (word C))              | (A B C)       | Yes      |
| (seq (word A) (word B) (word C))              | (X Y Z)       | No       |
| (seq (word A) (option (word B)) (word C))     | (A C)         | Yes      |
| (seq (word A) (option (word B)) (word C))     | (A B C)       | Yes      |
| (seq (word A) (option (word B)) (word C))     | (A B B C)     | No       |
| (seq (word A) (option (word B)) (word C))     | (A B B B C)   | No       |
| (seq (word A) (star   (word B)) (word C))     | (A C)         | Yes      |
| (seq (word A) (star   (word B)) (word C))     | (A B C)       | Yes      |
| (seq (word A) (star   (word B)) (word C))     | (A B B C)     | Yes      |
| (seq (word A) (star   (word B)) (word C))     | (A B B B C)   | Yes      |
| (seq (word A) (plus   (word B)) (word C))     | (A C)         | No       |
| (seq (word A) (plus   (word B)) (word C))     | (A B C)       | Yes      |
| (seq (word A) (plus   (word B)) (word C))     | (A B B C)     | Yes      |
| (seq (word A) (plus   (word B)) (word C))     | (A B B B C)   | Yes      |
| (seq (word A) (repeat (word B) 1 2) (word C)) | (A C)         | No       |
| (seq (word A) (repeat (word B) 1 2) (word C)) | (A B C)       | Yes      |
| (seq (word A) (repeat (word B) 1 2) (word C)) | (A B B C)     | Yes      |
| (seq (word A) (repeat (word B) 1 2) (word C)) | (A B B B C)   | No       |



#### Ordered Choice Rules

An *Ordered Choice Rule*, also known as a *Either Rule* combines a series of nested rules into a series of options, such that each nested rule will be applied, until one successfully matches. 

```bash
(either <option-0> ... <option-N>)
```

**Example Ordered Choice Rules:**

| Example Rule                                 | Example Input | Matches? |
|----------------------------------------------|---------------|----------|
| (either (word A) (word B) (word C))          | A             | Yes      |
| (either (word A) (word B) (word C))          | B             | Yes      |
| (either (word A) (word B) (word C))          | C             | Yes      |
| (either (word A) (word B) (word C))          | X             | No       |



#### And Rules

An *And Rule* facilitates looking ahead to determine whether a nested rule would match. 

**Syntax of an And Rule:**

```bash
(and <rule>)
```

**Example And Rules:**

| Example Rule                                 | Example Input | Matches? |
|----------------------------------------------|---------------|----------|
| (and (word A))                               | A             | Yes      |
| (and (word A))                               | B             | No       |



#### Not Rules

A *Not Rule* facilitates looking ahead to determine whether a nested rule would fail to match. 

**Syntax of a Not Rule:**

```bash
(not <rule>)
```

**Example Not Rules:**

| Example Rule                                 | Example Input | Matches? |
|----------------------------------------------|---------------|----------|
| (and (word A))                               | A             | No       |
| (and (word A))                               | B             | Yes      |



#### Predicate Rules

A *Predicate Rule* allows a user-defined [Predicate](https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/function/Predicate.html) to be used in order to determine whether a symbolic-expression matches. The predicate must be defined as part of Schema construction. 

**Syntax of a Predicate Rule:**

```bash
(predicate <name>)
```

**Example of a Predicate Rule:**

| Example Rule                                 | Example Input | Matches? |
|----------------------------------------------|---------------|----------|
| (predicate Planet)                           | Venus         | Yes      |
| (predicate Planet)                           | Ganymede      | No       |


**Example Usage of a Predicate Rule:**

```java
package examples;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.Set;
import java.util.function.Predicate;

public final class Example
{
    public static void main (String[] args)
    {
        final Predicate<Sexpr<?>> terrestrialPlanet = atom ->
        {
            final Set<String> planets = Set.of("Mercury", "Venus", "Earth", "Mars");
            return atom.isAtom() && planets.contains(atom.toString());
        };

        // Create a Schema that describes Symbolic Atoms,
        // such that the textual representations of the atoms
        // are the names of terrestrial planets in our solar system.
        final Schema schema = Schema.newBuilder()
                .include("(root = (predicate Planet))")
                .condition("Planet", terrestrialPlanet)
                .build();

        // Create some example atoms.
        final var atom1 = SAtom.fromString("Mercury");
        final var atom2 = SAtom.fromString("Venus");
        final var atom3 = SAtom.fromString("Earth");
        final var atom4 = SAtom.fromString("Mars");
        final var atom5 = SAtom.fromString("Europa");
        final var atom6 = SAtom.fromString("Titan");

        // Determine whether the atoms obey the Schema.
        System.out.println("Atom #1 = " + schema.match(atom1).isSuccess());
        System.out.println("Atom #2 = " + schema.match(atom2).isSuccess());
        System.out.println("Atom #3 = " + schema.match(atom3).isSuccess());
        System.out.println("Atom #4 = " + schema.match(atom4).isSuccess());
        System.out.println("Atom #5 = " + schema.match(atom5).isSuccess());
        System.out.println("Atom #6 = " + schema.match(atom6).isSuccess());
    }
}
```

**Output:**

```
Atom #1 = true
Atom #2 = true
Atom #3 = true
Atom #4 = true
Atom #5 = false
Atom #6 = false
```


#### Named Rules

As you have already observed in prior examples, rules can be assigned names. Those named rules can be used elsewhere in order to define complex, sometimes recursive, patterns. 

**Syntax of Rule Naming:**

```bash
(<name> = <rule>)
```

**Example Named Rules:**

| Example Rule                                   |
|------------------------------------------------|
| (mars   = (word Mars))                         |
| (earth  = (word Earth))                        |
| (planet = (either (word Saturn) (word Uranus)) |



#### Ref Rules

A *Ref Rule* allows one to use a named rule inside of another rule. 

**Syntax of Rule Naming:**

```bash
(ref <name>)
```

**Example Ref Rules:**

| Example Rule                                   |
|------------------------------------------------|
| (object = (either (ref Planet) (ref Moon))     |


#### Predefined Rules

Several named rules are predefined for convenience. By convention, the names of the predefined rules always start with a '$' symbol. More predefined rules may be added in the future. 

**List of Predefined Rules:**
* $ANY: Always matches. 
* $ATOM: Matches symbolic-atoms, but not symbolic-lists. 
* $LIST: Matches symbolic-lists, but not symbolic-atoms. 
* $BOOLEAN: Matches any symbolic-atom that can be converted to a boolean value. 
* $CHAR: Matches any symbolic-atom that can be converted to a char value. 
* $BYTE: Matches any symbolic-atom that can be converted to a byte value. 
* $SHORT: Matches any symbolic-atom that can be converted to a short value. 
* $INT: Matches any symbolic-atom that can be converted to a int value. 
* $LONG: Matches any symbolic-atom that can be converted to a long value. 
* $FLOAT: Matches any symbolic-atom that can be converted to a float value. 
* $DOUBLE: Matches any symbolic-atom that can be converted to a double value. 


### Matches

When a Symbolic Expression is matched against a Schema, a `Match` object is created. 



### Translation

*Translation* allows one to perform actions in response to a successful match of a Symbolic Expression. 



#### Initial Examples of Translation

Let us start of with a few examples of translation. 


**Example Prefix Notation Calculator:**

```java
package examples;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.Stack;

public final class Example
{
    private static final Stack<Double> stack = new Stack<>();

    public static void main (String[] args)
    {

        // Create a Schema that implements a stack-based calculator,
        // that uses prefix notation. Only four operations are supported,
        // namely division, multiplication, addition, and subtraction.
        final Schema schema = Schema.newBuilder()
                .include("(root = (seq (ref operand)))")
                .include("(operand = (either (ref number) (ref operation)))")
                .include("(operation = (either (ref div) (ref mul) (ref add) (ref sub)))")
                .include("(div = (seq (word /) (ref operand) (ref operand)))")
                .include("(mul = (seq (word *) (ref operand) (ref operand)))")
                .include("(add = (seq (word +) (ref operand) (ref operand)))")
                .include("(sub = (seq (word -) (ref operand) (ref operand)))")
                .include("(number = (ref $DOUBLE))")
                .pass("calc")
                .before("calc", "number", x -> stack.push(x.asAtom().asDouble().get()))
                .after("calc", "div", Example::operationDiv)
                .after("calc", "mul", Example::operationMul)
                .after("calc", "add", Example::operationAdd)
                .after("calc", "sub", Example::operationSub)
                .after("calc", "root", x -> System.out.println(stack.pop()))
                .build();

        // Evaluate an example expression: ((((100 / 4) + 2) * 3) - 5)
        schema.match(SList.parse("(- (* (+ (/ 100 4) 2) 3) 5)")).execute();
    }

    private static void operationDiv (final Sexpr<?> node)
    {
        final double right = stack.pop();
        final double left = stack.pop();
        final double result = left / right;
        stack.push(result);
    }

    private static void operationMul (final Sexpr<?> node)
    {
        final double right = stack.pop();
        final double left = stack.pop();
        final double result = left * right;
        stack.push(result);
    }

    private static void operationAdd (final Sexpr<?> node)
    {
        final double right = stack.pop();
        final double left = stack.pop();
        final double result = left + right;
        stack.push(result);
    }

    private static void operationSub (final Sexpr<?> node)
    {
        final double right = stack.pop();
        final double left = stack.pop();
        final double result = left - right;
        stack.push(result);
    }
}
```

**Output:**

```
76.0
```

#### Passes

*Translation* is performed in a series of user-defined *passes*. In effect, passes facilitate the implementation of [multi-pass compiler](https://en.wikipedia.org/wiki/Multi-pass_compiler).


#### Before Actions

#### After Actions

#### Defined Via Annotations