package src.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.util.Tuple;

public class ExportBillPerItem {

	@DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
	private int id;
	private static int idCounter = 0;
	@DAttr(name = "product", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "product-import", role = "import", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = Product.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private Product product;

	@DAttr(name = "equantity", type = Type.Integer, length = 10, optional = true, min = 0.0)
	private Integer equantity;

	@DAttr(name = "eprice", type = Type.Double, length = 15, optional = true, min = 0.0)
	private Double eprice;

	@DAttr(name = "exportChecker", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "product-exportChecker", role = "exportChecker", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = ExportChecker.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private ExportChecker exportChecker;

	@DAttr(name = "exportBill", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "exportBill-has-exportBillPerItem", role = "exportBill", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = ExportBill.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private ExportBill exportBill;

//	@DAttr(name = "totalPrice",type=Type.Double,auto=true,mutable = false,optional = true,
//		      serialisable=false,
//		      derivedFrom={"equantity", "eprice"})
	@DAttr(name = "totalPrice", type = Type.Double, length = 15, optional = true, min = 0.0)
	private Double totalPrice;

	// private StateHistory<String, Object> stateHist;

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	@DOpt(type = DOpt.Type.RequiredConstructor)
	public ExportBillPerItem(@AttrRef("product") Product p, @AttrRef("equantity") Integer equantity,
			@AttrRef("eprice") Double eprice, @AttrRef("exportChecker") ExportChecker exportChecker,
			@AttrRef("exportBill") ExportBill exportBill, @AttrRef("totalPrice") Double totalPrice)
			throws ConstraintViolationException {
		this(null, p, equantity, eprice, exportChecker, exportBill, totalPrice);

	}

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public ExportBillPerItem(Integer id, Product p, Integer equantity, Double eprice,
			ExportChecker exportChecker, ExportBill exportBill, Double totalPrice) throws ConstraintViolationException {
		this.id = nextID(id);
		this.product = p;
		this.equantity = equantity;
		this.eprice = eprice;
		this.exportChecker = exportChecker;
		this.exportBill = exportBill;
		this.totalPrice = totalPrice;
//		//this.importer = importer;
//		stateHist = new StateHistory<>();
//		updateTotalPrice();
	}

	private static int nextID(Integer currID) {
		if (currID == null) {
			idCounter++;
			return idCounter;
		} else {
			int num;
			num = currID.intValue();
			if (num > idCounter) {
				idCounter = num;
			}
			return currID;
		}
	}

	public int getId() {
		return id;
	}

//	public void setId(int id) {
//		this.id = id;
//	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

//	public String getEdate() {
//		return edate;
//	}
//
//	public void setEdate(String edate) {
//		this.edate = edate;
//	}

	public Integer getEquantity() {
		return equantity;
	}

	public void setIquantity(Integer equantity) {
		this.equantity = equantity;
		// updateTotalPrice();
	}

//	public ImportStaff getImporter() {
//		return importer;
//	}
//
//	public void setImporter(ImportStaff importer) {
//		this.importer = importer;
//	}

	public Double getEprice() {
		return eprice;
	}

	public void setEprice(Double eprice) {
		this.eprice = eprice;
		// updateTotalPrice();
	}

	public ExportChecker getExportChecker() {
		return exportChecker;
	}

	public void setExportChecker(ExportChecker exportChecker) {
		this.exportChecker = exportChecker;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setExportBill(ExportBill exportBill) {
		this.exportBill = exportBill;
	}

	public ExportBill getExportBill() {
		return exportBill;
	}

//	public Double getTotalPrice(boolean cached) throws IllegalStateException {
//	    if (cached) {
//	      Object val = stateHist.get("totalPrice");
//
//	      if (val == null)
//	        throw new IllegalStateException(
//	            "Enrolment.getFinalMark: cached value is null");
//
//	      return (Double) val;
//	    } else {
//	      if (totalPrice != null)
//	        return totalPrice;
//	      else
//	        return 0.0;
//	    }
//
//	  }
//	

//
//	public void setTotalPrice(int totalPrice) {
//		this.totalPrice = totalPrice;
//		updateTotalPrice();
//	}
	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
			throws ConstraintViolationException {
		if (minVal != null && maxVal != null) {
			// check the right attribute
			if (attrib.name().equals("id")) {
				int maxIdVal = (Integer) maxVal;
				if (maxIdVal > idCounter)
					idCounter = maxIdVal;
			}
		}
	}
//	  @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
//	  @AttrRef(value="totalPrice")
//	  public void updateTotalPrice() {
//		  if(equantity != null && eprice != null) {
//			  double finalMarkD = equantity * eprice;
//			  stateHist.put("totalPrice", finalMarkD);
//			  //totalPrice = (int) Math.round(finalMarkD);
//		  }
//	  }

}
