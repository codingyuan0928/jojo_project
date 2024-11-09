package com.group1.gosports_jojo.customersupport.model;


public class CustomerSupportDTO {

    private Integer formId;

    private String errorMsgs;
    
    
	public CustomerSupportDTO() {
		super();
	}

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public String getErrorMsgs() {
		return errorMsgs;
	}

	public void setErrorMsgs(String errorMsgs) {
		this.errorMsgs = errorMsgs;
	}


}
