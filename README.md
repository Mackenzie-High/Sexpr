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




#### Ordered Choice Rules

`(either option-1 ... option-N)`


#### And Rules

#### Not Rules

#### Predicate Rules

### Match Results

When a Symbolic Expression is matched against a Schema, a `MatchResult` is created. 


### Translation



#### Passes

#### Before Actions

#### After Actions

#### Defined Via Annotations