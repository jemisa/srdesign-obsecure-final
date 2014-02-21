/*
 *
 */
package cs492.obsecurefinal.obsecurecyc.opencyc.util.query;

//// External Imports
import java.util.List;

//// Internal Imports
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycList;
import cs492.obsecurefinal.obsecurecyc.opencyc.cycobject.CycVariable;
import cs492.obsecurefinal.obsecurecyc.opencyc.util.query.DefaultQueryResult.DefaultBinding;
import cs492.obsecurefinal.obsecurecyc.opencyc.util.query.QueryResult.Binding;

/**
 *
 * @author baxter
 */
public class QueryResultFactory {

  public static Binding parseBinding(final CycList rawBinding) {
    if (rawBinding.size() == 2) {
      final CycVariable variable = (CycVariable) rawBinding.get(0);
      final Object term = rawBinding.getDottedElement();
      return new DefaultBinding(variable, term);
    }
    return null;
  }

  public static QueryResult constructResult(List<Binding> bindings) {
    return new DefaultQueryResult(bindings, null);
  }
}
