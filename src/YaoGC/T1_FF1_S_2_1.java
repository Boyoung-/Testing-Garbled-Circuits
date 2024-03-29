// Copyright (C) 2010 by Yan Huang <yhuang@virginia.edu>

package YaoGC;

import java.math.*;

import Cipher.*;

public abstract class T1_FF1_S_2_1 extends SimpleCircuit_2_1 {
    public T1_FF1_S_2_1() {
	super("T1_FF1_S_2_1");
    }

    public static T1_FF1_S_2_1 newInstance() {
	if (Circuit.isForGarbling)
	    return new G_T1_FF1_S_2_1();
	else
	    return new E_T1_FF1_S_2_1();
    }

    private int output(int l, int r) {
	int o;
	if (l == 0) {
	    if (r == 0)
		o = 0;
	    else
		o = 1;
	}
	else {
	    if (r == 0)
		o = 1;
	    else
		o = 1;
	}
	return o;
    }

    protected void compute() {
	int left  = inputWires[0].value;
	int right = inputWires[1].value;

	outputWires[0].value = output(left, right);
    }

    protected void fillTruthTable() {
	Wire inWireL = inputWires[0];
	Wire inWireR = inputWires[1];
	Wire outWire = outputWires[0];

    	BigInteger[] labelL = {inWireL.lbl, Wire.conjugate(inWireL.lbl)};
    	if (inWireL.invd == true) {
    	    BigInteger tmp = labelL[0];
    	    labelL[0] = labelL[1];
    	    labelL[1] = tmp;
    	}
	    
    	BigInteger[] labelR = {inWireR.lbl, Wire.conjugate(inWireR.lbl)};
    	if (inWireR.invd == true) {
    	    BigInteger tmp = labelR[0];
    	    labelR[0] = labelR[1];
    	    labelR[1] = tmp;
    	}

    	int k = outWire.serialNum;

	gtt = new BigInteger[2][2];

	int cL = inWireL.lbl.testBit(0) ? 1 : 0;
	int cR = inWireR.lbl.testBit(0) ? 1 : 0;

	BigInteger[] lb = new BigInteger[2];
	lb[output(cL, cR)] = Cipher.encrypt(labelL[cL], labelR[cR], k, BigInteger.ZERO);
	lb[1 - output(cL, cR)] = Wire.conjugate(lb[output(cL, cR)]);
	outWire.lbl = lb[0];

	gtt[0 ^ cL][0 ^ cR] = lb[0];
	gtt[0 ^ cL][1 ^ cR] = lb[1];
	gtt[1 ^ cL][0 ^ cR] = lb[1];
	gtt[1 ^ cL][1 ^ cR] = lb[1];
    }

    protected boolean shortCut() {
	/*
	if (inputWires[0].value == 1) {
	    inputWires[0].value = Wire.UNKNOWN_SIG;
	    outputWires[0].value = 1;
	    return true;
	}
	
	if (inputWires[1].value == 1) {
	    inputWires[1].value = Wire.UNKNOWN_SIG;
	    outputWires[0].value = 1;
	    return true;
	}
	*/

	return false;
    }

    protected boolean collapse() {
    	Wire inWireL = inputWires[0];
    	Wire inWireR = inputWires[1];
    	Wire outWire = outputWires[0];

    	if (inWireL.lbl.equals(inWireR.lbl)) {
	    // TODO: rarely happen, but customize below
    	    if (inWireL.invd == inWireR.invd) {
    		outWire.invd = inWireL.invd;
    		outWire.setLabel(inWireL.lbl);
    	    }
    	    else {
    		outWire.invd = false;
    		outWire.value = 1;
    	    }

    	    return true;
    	}

    	return false;
    }
}
