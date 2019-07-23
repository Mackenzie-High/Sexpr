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

A Schema is a pattern that describes a Symbolic Expression tree data-structure. Schemas are closely related to formal grammars used by parser generators. In particular, schemas are based on Parsing Expression Grammars conceptually. As such, a schema consists of a series of rules that can be used to *match* an expression. 


### Hello World Schema

```java
package examples;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema;
import com.mackenziehigh.sexpr.Schema.MatchResult;

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
                .include("nil", "(root = (seq (ref verb) (ref planet)))")
                .include("nil", "(verb = (either (keyword Hello) (keyword Goodbye)))")
                .include("nil", "(planet = (either (keyword Earth) (keyword Mars)))")
                .build();

        // Create a Symbolic List containing two Symbolic Atoms. 
        // Notice that the enclosing parentheses are implicit. 
        final SList input = SList.parse("Hello Earth");
        System.out.println("Input = " + input);

        // Determine whether the Symbolic List obeys the afore Schema. 
        final MatchResult result = schema.match(input);
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

#### Root Rules

#### Keyword Rules

`(keyword text)`

#### Atom Rules

`(atom text)`

#### Named Rules

`(name = rule)`

#### Sequence Rules

`(seq option-1 ... option-N)`

`(option rule)`

`(star rule)`

`(plus rule)`

`(repeat rule minimum maximum)`


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
