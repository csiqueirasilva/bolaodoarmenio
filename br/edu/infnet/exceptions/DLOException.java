package br.edu.infnet.exceptions;

public class DLOException extends Exception {

	public DLOException() {
	}

	public DLOException(String arg0) {
		super(arg0);
	}

	public DLOException(Throwable arg0) {
		super(arg0);
	}

	public DLOException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DLOException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
