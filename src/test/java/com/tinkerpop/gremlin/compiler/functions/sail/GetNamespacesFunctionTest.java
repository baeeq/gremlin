package com.tinkerpop.gremlin.compiler.functions.sail;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.sail.SailGraph;
import com.tinkerpop.gremlin.BaseTest;
import com.tinkerpop.gremlin.compiler.Atom;
import com.tinkerpop.gremlin.compiler.GremlinEvaluator;
import com.tinkerpop.gremlin.compiler.Tokens;
import com.tinkerpop.gremlin.compiler.functions.Function;
import org.openrdf.sail.memory.MemoryStore;

import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class GetNamespacesFunctionTest extends BaseTest {

    public void testGetNamespaces() {
        SailGraph graph = new SailGraph(new MemoryStore());
        GremlinEvaluator.declareVariable(Tokens.GRAPH_VARIABLE, new Atom<Graph>(graph));
        Function<Map<Atom<String>,Atom<String>>> function = new GetNamespacesFunction();
        this.stopWatch();
        Atom<Map<Atom<String>,Atom<String>>> atom = function.compute(createUnaryArgs(graph));
        printPerformance(function.getFunctionName() + " function", 1, "evaluation", this.stopWatch());
        assertTrue(atom.getValue().containsKey(new Atom<String>("rdf")));
        assertTrue(atom.getValue().containsKey(new Atom<String>("rdfs")));
        assertTrue(atom.getValue().containsKey(new Atom<String>("owl")));
        assertTrue(atom.getValue().containsKey(new Atom<String>("foaf")));


        this.stopWatch();
        atom = function.compute(createUnaryArgs());
        printPerformance(function.getFunctionName() + " function", 1, "evaluation", this.stopWatch());
        assertTrue(atom.getValue().containsKey(new Atom<String>("rdf")));
        assertTrue(atom.getValue().containsKey(new Atom<String>("rdfs")));
        assertTrue(atom.getValue().containsKey(new Atom<String>("owl")));
        assertTrue(atom.getValue().containsKey(new Atom<String>("foaf")));

        graph.shutdown();
    }
}