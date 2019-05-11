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

public class ImportBillPerItem {
	@DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
	private int id;
	private static int idCounter = 0;
	@DAttr(name = "product", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "product-export", role = "export", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = Product.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private Product product;

	@DAttr(name = "iquantity", type = Type.Integer, length = 10, optional = true, min = 0.0)
	private Integer iquantity;

	@DAttr(name = "iprice", type = Type.Double, length = 15, optional = true, min = 0.0)
	private Double iprice;

	@DAttr(name = "importChecker", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "product-importChecker", role = "importChecker", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = ImportChecker.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private ImportChecker importChecker;

	@DAttr(name = "importBill", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "importBill-has-importBillPerItem", role = "importBill", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = ImportBill.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private ImportBill importBill;

//	@DAttr(name = "totalPrice",type=Type.Double,auto=true,mutable = false,optional = true,
//		      serialisable=false,
//		      derivedFrom={"equantity", "eprice"})
	@DAttr(name = "totalPrice", type = Type.Double, length = 15, optional = true, min = 0.0)
	private Double totalPrice;

	// private StateHistory<String, Object> stateHist;

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	@DOpt(type = DOpt.Type.RequiredConstructor)
	public ImportBillPerItem(@AttrRef("product") Product p, @AttrRef("iquantity") Integer iquantity,
			@AttrRef("iprice") Double iprice, @AttrRef("importChecker") ImportChecker importChecker,
			@AttrRef("importBill") ImportBill importBill, @AttrRef("totalPrice") Double totalPrice)
			throws ConstraintViolationException {
		this(null, p, iquantity, iprice, importChecker, importBill, totalPrice);
	}

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public ImportBillPerItem(Integer id, Product p, Integer iquantity, Double iprice,
			ImportChecker importChecker, ImportBill importBill, Double totalPrice) throws ConstraintViolationException {
		this.id = nextID(id);
		this.product = p;
		this.iquantity = iquantity;
		this.iprice = iprice;
		this.importChecker = importChecker;
		this.importBill = importBill;
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

	public Integer getIquantity() {
		return iquantity;
	}

	public void setIquantity(Integer iquantity) {
		this.iquantity = iquantity;
		// updateTotalPrice();
	}

//	public ImportStaff getImporter() {
//		return importer;
//	}
//
//	public void setImporter(ImportStaff importer) {
//		this.importer = importer;
//	}

	public Double getIprice() {
		return iprice;
	}

	public void setIprice(Double iprice) {
		this.iprice = iprice;
		// updateTotalPrice();
	}

	public ImportChecker getImportChecker() {
		return importChecker;
	}

	public void setImportChecker(ImportChecker importChecker) {
		this.importChecker = importChecker;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setImportBill(ImportBill importBill) {
		this.importBill = importBill;
	}

	public ImportBill getImportBill() {
		return importBill;
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
