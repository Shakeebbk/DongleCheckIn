package com.example.donglecheckin;

public class DCILogClass {
	public DCIConfig.CheckStatus status;
	String timestamp;
	String location;

	public DCILogClass(DCIConfig.CheckStatus status, String timestamp, String location) {
		super();
		this.status = status;
		this.location = location;
		this.timestamp = timestamp;
	}
}
