package com.tinkerpop.gremlin.compiler.pipes;

import com.tinkerpop.gremlin.compiler.context.GremlinScriptContext;
import com.tinkerpop.gremlin.compiler.functions.Function;
import com.tinkerpop.gremlin.compiler.operations.Operation;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.filter.FilterPipe;

import java.util.List;

/**
 * [g:func()]
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class FunctionFilterPipe<S> extends AbstractPipe<S, S> implements FilterPipe<S> {

    private final Function function;
    private final List<Operation> arguments;
    private final GremlinScriptContext context;
    private int counter = -1;
    private int index = -1;
    private boolean numberMode = false;

    public FunctionFilterPipe(Function function, List<Operation> arguments, final GremlinScriptContext context) {
        this.function = function;
        this.arguments = arguments;
        this.context = context;
    }

    public S processNextStart() {
        while (true) {
            S s = this.starts.next();
            this.context.setCurrentPoint(s);

            if (this.numberMode) {
                this.counter++;
                if (this.counter == this.index) {
                    return s;
                }
            } else {
                Object functionReturn = this.function.compute(GremlinPipesHelper.updateArguments(this.arguments, s), this.context).getValue();
                if (functionReturn instanceof Boolean && ((Boolean) functionReturn)) {
                    return s;
                } else if (functionReturn instanceof Number) {
                    this.numberMode = true;
                    this.index = ((Number) functionReturn).intValue();
                    this.counter++;
                    if (this.counter == this.index) {
                        return s;
                    }
                }
            }
        }
    }
}
