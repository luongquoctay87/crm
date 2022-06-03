package crm.wealth.management.api.response;

import java.io.Serializable;

public class TokenResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String token;

	public TokenResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return this.token;
	}
}