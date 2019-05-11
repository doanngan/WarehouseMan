package src.model;

	import java.util.Collection;
	import java.util.Map;

	import domainapp.basics.core.dodm.dsm.DSMBasic;
	import domainapp.basics.core.dodm.qrm.QRM;
	import domainapp.basics.exceptions.DataSourceException;
	import domainapp.basics.exceptions.NotPossibleException;
	import domainapp.basics.model.Oid;
	import domainapp.basics.model.meta.AttrRef;
	import domainapp.basics.model.meta.DAssoc;
	import domainapp.basics.model.meta.DAssoc.AssocEndType;
	import domainapp.basics.model.meta.DAssoc.AssocType;
	import domainapp.basics.model.meta.DAssoc.Associate;
	import domainapp.basics.model.meta.DAttr;
	import domainapp.basics.model.meta.DAttr.Type;
	import domainapp.basics.model.meta.DClass;
	import domainapp.basics.model.meta.DOpt;
	import domainapp.basics.model.meta.MetaConstants;
	import domainapp.basics.model.meta.Select;
	import domainapp.basics.model.query.Expression.Op;
	import domainapp.basics.model.query.Query;
	import domainapp.basics.model.query.QueryToolKit;
	import domainapp.basics.modules.report.model.meta.Output;
	import src.model.TypeOfProduct;
	import src.model.ProductsByTypeReport;

	/**
	 * @overview 
	 * 	Represent the reports about product by type.
	 *
	 */
	@DClass(schema="WarehouseMan",serialisable=false) //true
	public class ProductsByTypeReport {
	  @DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
	  private int id;
	  private static int idCounter = 0;

	  /**input: TypeofProduct name */
	  @DAttr(name = "typeOfProduct", type = Type.String, length = 30, optional = false)
	  private String typeOfProduct;
	  
	  /**output: TypeOfProducts whose names match {@link #typeOfProduct} */
	  @DAttr(name="TypeOfProducts",type=Type.Collection,optional=false, mutable=false,
	      serialisable=false,filter=@Select(clazz=TypeOfProduct.class, 
	      attributes={TypeOfProduct.B_id, TypeOfProduct.B_name, TypeOfProduct.B_rptProductByType})
	      ,derivedFrom={"typeOfProduct"}
	      )
	  @DAssoc(ascName="Products-by-type-report-has-TypeOfProducts",role="report",
	      ascType=AssocType.One2Many,endType=AssocEndType.One,
	    associate=@Associate(type=TypeOfProduct.class,cardMin=0,cardMax=MetaConstants.CARD_MORE
	    ))
	  @Output
	  private Collection<TypeOfProduct> TypeOfProducts;

	  /**output: number of Products found (if any), derived from {@link #TypeOfProducts} */
	  @DAttr(name = "numProducts", type = Type.Integer, length = 20, auto=true, mutable=false)
	  @Output
	  private int numProducts;
	  
	  /**
	   * @param typeOfProduct 
	 * @effects 
	   *  initialise this with <tt>name</tt> and use {@link QRM} to retrieve from data source 
	   *  all {@link TypeOfProduct} whose names match <tt>name</tt>.
	   *  initialise {@link #TypeOfProducts} with the result if any.
	   *  
	   *  <p>throws NotPossibleException if failed to generate data source query; 
	   *  DataSourceException if fails to read from the data source
	   * 
	   */
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  @DOpt(type=DOpt.Type.RequiredConstructor)
	  public ProductsByTypeReport(@AttrRef("typeOfProduct") String name, String typeOfProduct) throws NotPossibleException, DataSourceException {
	    this.id=++idCounter;
	    this.typeOfProduct = typeOfProduct;
	    
	    doReportQuery();
	  }
	  
	  /**
	   * @effects return typeOfProduct
	   */
	  public String getTypeOfProduct() {
	    return typeOfProduct;
	  }

	  /**
	   * @effects <pre>
	   *  set this.typeOfProduct = typeOfProduct
	   *  if typeOfProduct is changed
	   *    invoke {@link #doReportQuery()} to update the output attribute value
	   *    throws NotPossibleException if failed to generate data source query; 
	   *    DataSourceException if fails to read from the data source.
	   *  </pre>
	   */
	  public void setTypeOfProduct(String typeOfProduct) throws NotPossibleException, DataSourceException {
    
	    this.typeOfProduct = typeOfProduct;
	    
	    // DONOT invoke this here if there are > 1 input attributes!
	    doReportQuery();
	  }

	  /**
	   * This method is invoked when the report input has be set by the user. 
	   * 
	   * @effects <pre>
	   *   formulate the object query
	   *   execute the query to retrieve from the data source the domain objects that satisfy it 
	   *   update the output attributes accordingly.
	   *  
	   *  <p>throws NotPossibleException if failed to generate data source query; 
	   *  DataSourceException if fails to read from the data source. </pre>
	   */
	  @DOpt(type=DOpt.Type.DerivedAttributeUpdater) //
	  @AttrRef(value="TypeOfProducts")
	  public void doReportQuery() throws NotPossibleException, DataSourceException {
	    // the query manager instance
	    
	    QRM qrm = QRM.getInstance();
	    
	    // create a query to look up TypeOfProduct from the data source
	    // and then populate the output attribute (TypeOfProducts) with the result
	    DSMBasic dsm = qrm.getDsm();
	    
	    //TODO: to conserve memory cache the query and only change the query parameter value(s)
	    Query q = QueryToolKit.createSearchQuery(dsm, TypeOfProduct.class, 
	        new String[] {TypeOfProduct.B_name}, 
	        new Op[] {Op.MATCH}, 
	        new Object[] {"%"+typeOfProduct+"%"});
	    
	    Map<Oid, TypeOfProduct> result = qrm.getDom().retrieveObjects(TypeOfProduct.class, q);
	    
	    if (result != null) {
	      // update the main output data 
	      TypeOfProducts = result.values();
	      
	      // update other output (if any)
	      numProducts = TypeOfProducts.size();
	    } else {
	      // no data found: reset output
	      resetOutput();
	    }
	  }

	  /**
	   * @effects 
	   *  reset all output attributes to their initial values
	   */
	  private void resetOutput() {
	    TypeOfProducts = null;
	    numProducts = 0;
	  }

	  /**
	   * A link-adder method for {@link #TypeOfProducts}, required for the object form to function.
	   * However, this method is empty because TypeOfProducts have already be recorded in the attribute {@link #TypeOfProducts}.
	   */
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addTypeOfProduct(Collection<TypeOfProduct> TypeOfProducts) {
	    // do nothing
	    return false;
	  }
	  
	  /**
	   * @effects return TypeOfProducts
	   */
	  public Collection<Product> getProducts() {
	    return Products;
	  }
	  
	  /**
	   * @effects return numTypeOfProducts
	   */
	  public int getNumProducts() {
	    return numProducts;
	  }

	  /**
	   * @effects return id
	   */
	  public int getId() {
	    return id;
	  }

	  /* (non-Javadoc)
	   * @see java.lang.Object#hashCode()
	   */
	  /**
	   * @effects 
	   * 
	   * @version 
	   */
	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + id;
	    return result;
	  }

	  /* (non-Javadoc)
	   * @see java.lang.Object#equals(java.lang.Object)
	   */
	  /**
	   * @effects 
	   * 
	   * @version 
	   */
	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    ProductsByTypeReport other = (ProductsByTypeReport) obj;
	    if (id != other.id)
	      return false;
	    return true;
	  }

	  /* (non-Javadoc)
	   * @see java.lang.Object#toString()
	   */
	  /**
	   * @effects 
	   * 
	   * @version 
	   */
	  @Override
	  public String toString() {
	    return "ProductsByTypeReport (" + id + ", " + typeOfProduct + ")";
	  }
}
