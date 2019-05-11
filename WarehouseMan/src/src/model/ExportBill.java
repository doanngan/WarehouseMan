package src.model;

import java.util.ArrayList;
import java.util.Collection;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;

public class ExportBill {

	private static int idCounter = 0;
	@DAttr(name = "id", id = true, auto = true, type = Type.String, length = 6, mutable = false, optional = false)
	private String id;

	@DAttr(name = "exportStaff", type = Type.Domain, length = 5, optional = false)
	@DAssoc(ascName = "product-exportStaff", role = "exportStaff", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = ExportStaff.class, cardMin = 1, cardMax = 1), dependsOn = true)
	private ExportStaff exportStaff;
	

	@DAttr(name = "date", type = Type.String, length = 30, optional = false)
	private String date;

	@DAttr(name = "exportBillPerItem", type = Type.Collection, optional = false, serialisable = false, filter = @Select(clazz = ExportBillPerItem.class))
	@DAssoc(ascName = "exportBill-has-exportBillPerItem", role = "exportBill", ascType = AssocType.One2Many, endType = AssocEndType.One, associate = @Associate(type = ExportBillPerItem.class, cardMin = 0, cardMax = 30))
	private Collection<ExportBillPerItem> exportBillPerItem;

	private int count;

	@DOpt(type = DOpt.Type.DataSourceConstructor)
	public ExportBill(String id, ExportStaff exportStaff, String date) {
		this.id = nextID(id);
		this.exportStaff = exportStaff;
		this.date = date;
		exportBillPerItem = new ArrayList<>();
		count = 0;
	}

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	@DOpt(type = DOpt.Type.RequiredConstructor)
	public ExportBill(@AttrRef("exportStaff") ExportStaff exportStaff, 
			@AttrRef("date") String date) {
		this(null,exportStaff , date);
	}

	public ExportStaff getExportStaff() {
		return exportStaff;
	}

	public void setExportStaff(ExportStaff exportStaff) {
		this.exportStaff=exportStaff;
	}
	
	

//		public String getProduct() {
//			return product;
//		}
	//
//		public void setProduct(String product) {
//			this.product = product;
//		}
	//
//		public Integer getUnitPrice() {
//			return unitPrice;
//		}
	//
//		public void setUnitPrice(Integer unitPrice) {
//			this.unitPrice = unitPrice;
//			calTotal();
//		}
	//
//		public Integer getQuantity() {
//			return quantity;
//		}
	//
//		public void setQuantity(Integer quantity) {
//			this.quantity = quantity;
//			calTotal();
//		}
//

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

//		public Integer getTotalPrice() {
//			return totalPrice;
//		}

	@DOpt(type = DOpt.Type.LinkAdder)
	public boolean addExportBillPerItem(ExportBillPerItem d) {
		if (!exportBillPerItem.contains(d))
			exportBillPerItem.add(d);

		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewExportBillPerItem(ExportBillPerItem d) {
		exportBillPerItem.add(d);

		count++;

		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	// @MemberRef(name="enrolments")
	public boolean addExportBillPerItem(Collection<ExportBillPerItem> deos) {
		boolean added = false;
		for (ExportBillPerItem d : deos) {
			if (!exportBillPerItem.contains(d)) {
				if (!added)
					added = true;
				exportBillPerItem.add(d);
			}
		}
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewExportBillPerItem(Collection<ExportBillPerItem> deos) {
		exportBillPerItem.addAll(deos);
		count += deos.size();

		return false;
	}

	@DOpt(type = DOpt.Type.LinkRemover)
	public boolean removeExportBillPerItem(ExportBillPerItem d) {
		boolean removed = exportBillPerItem.remove(d);

		if (removed) {
			count--;

		}
		return false;
	}

	public void setExportBillPerItem(Collection<ExportBillPerItem> deo) {
		this.exportBillPerItem = deo;
		count = deo.size();

	}

	public Collection<ExportBillPerItem> getExportBillPerIteml() {
		return exportBillPerItem;
	}

	@DOpt(type = DOpt.Type.LinkCountGetter)
	public int getCount() {
		return count;
		// return enrolments.size();
	}

	@DOpt(type = DOpt.Type.LinkCountSetter)
	public void setCount(int count1) {
		count = count1;
	}

	@Override
	public String toString() {
		return "Export2(" + id + "," + exportStaff  + "," + date + ")";
	}

	public String nextID(String id) throws ConstraintViolationException {
		if (id == null) { // generate a new id
			idCounter++;

			return "SO" + idCounter;
		} else {
			// update id
			int num;
			try {
				num = Integer.parseInt(id.substring(2));
			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { id });
			}

			if (num > idCounter) {
				idCounter = num;
			}

			return id;
		}
	}

	/**
	 * @requires minVal != null /\ maxVal != null
	 * @effects update the auto-generated value of attribute <tt>attrib</tt>,
	 *          specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
	 */
	@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
	public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
			throws ConstraintViolationException {

		if (minVal != null && maxVal != null) {
			// TODO: update this for the correct attribute if there are more than one auto
			// attributes of this class

			String maxId = (String) maxVal;

			try {
				int maxIdNum = Integer.parseInt(maxId.substring(2));

				if (maxIdNum > idCounter) // extra check
					idCounter = maxIdNum;

			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { maxId });
			}
		}
	}

}

