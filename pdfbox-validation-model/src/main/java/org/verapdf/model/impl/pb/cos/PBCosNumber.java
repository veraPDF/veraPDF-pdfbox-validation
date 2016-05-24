package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosNumber;

/**
 * Current class is representation of CosNumber interface of abstract model.
 * Methods of this class using in PBCosInteger and PBCosReal. This class is
 * analogue of COSNumber in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 * @see PBCosInteger
 * @see PBCosReal
 */
public abstract class PBCosNumber extends PBCosObject implements CosNumber {

    private final long longVal;
    private final double doubleVal;

    protected PBCosNumber(COSNumber number, final String type) {
        super(number, type);
        this.longVal = number.longValue();
        this.doubleVal = number.doubleValue();
    }

    public static PBCosNumber fromPDFBoxNumber(COSBase number) {
        if (number instanceof COSInteger) {
            return new PBCosInteger((COSInteger) number);
        } else if (number instanceof COSFloat) {
            return new PBCosReal((COSFloat) number);
        }
        return null;
    }

    /**
     * Get the string representing this object
     */
    @Override
    public String getstringValue() {
        return String.valueOf(this.doubleVal);
    }

    /**
     * Get truncated integer value
     */
    @Override
    public Long getintValue() {
        return Long.valueOf(this.longVal);
    }

    /**
     * Get original decimal value
     */
    @Override
    public Double getrealValue() {
        return Double.valueOf(this.doubleVal);
    }
}
