package org.verapdf.model.impl.pb.pd.functions;

import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDFunction;

public class PBoxPDFunction extends PBoxPDObject implements PDFunction {

    public static final String PD_FUNCTION_TYPE = "PDFunction";

    protected PBoxPDFunction(org.apache.pdfbox.pdmodel.common.function.PDFunction function, String type) {
        super(function, type);
    }

    public PBoxPDFunction(org.apache.pdfbox.pdmodel.common.function.PDFunction function) {
        super(function, PD_FUNCTION_TYPE);
    }

    @Override
    public Long getFunctionType() {
        return null;
    }
}
