package com.rageps.net.rest;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.rageps.net.rest.payments.Invoice;
import com.rageps.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Ryley Kimmel on 12/3/2017.
 */
public final class RestfulNexus {

	public static final RestfulNexus NEXUS = RestfulNexusProvider.create().provide();

	public static final Logger LOGGER = LogManager.getLogger();

	private static final Gson GSON = new Gson();

	private final String url;
	private final String apiKey;

	public RestfulNexus(String url, String apiKey) {
		this.url = url;
		this.apiKey = apiKey;
	}

	/*
	public boolean addAccount(String username, String passwordHash, String passwordSalt, String ipAddress) throws UnirestException {
		GetRequest request = Unirest.get(url + "forums/insert/{username}/{email}/{time}/{hash}/{salt}/{ip_address}/{seo_name}");
		request.header("key", apiKey);
		request.routeParam("username", username);
		request.routeParam("email", StringUtil.encodeUsername(username) + "@rageps.io");
		request.routeParam("time", String.valueOf(System.currentTimeMillis() / 1000));
		request.routeParam("ip_address", ipAddress);
		request.routeParam("seo_name", username.toLowerCase());
		request.routeParam("salt", passwordSalt);
		request.routeParam("hash", passwordHash);

		HttpResponse<String> response = request.asString();
		return Boolean.parseBoolean(response.getBody());
	}

	public ForumAccount getAccount(String username) throws UnirestException {
		//GetRequest request = Unirest.get(url + "forums/select/{username}");
		GetRequest request = Unirest.get(url + "/public/forums/api/index.php?/core/members/0");
		request.header("apiKey", apiKey);
		//request.routeParam("username", username);

		HttpResponse<String> response = request.asString();

		LOGGER.info(request.getUrl());
		LOGGER.info("headers {}",response.getHeaders());
		LOGGER.info("body {}",response.getBody());
		LOGGER.info("status {} {}", response.getStatusText(), response.getStatus());
		ForumCredentials[] credentials = GSON.fromJson(response.getBody(), ForumCredentials[].class);
		if (credentials.length == 0) {
			return null;
		}

		return ForumAccount.fromCredentials(username, credentials[0]);
	}*/

	public double getUnspentPointBalance(String username) throws UnirestException {
		GetRequest request = Unirest.get(url + "store/points/{username}");
		request.header("key", apiKey);
		request.routeParam("username", username);

		HttpResponse<String> response = request.asString();
		return Double.valueOf(response.getBody());
	}

	public double getTotalSpent(String username) throws UnirestException {
		GetRequest request = Unirest.get(url + "points/{username}");
		request.header("key", apiKey);
		request.routeParam("username", username);

		HttpResponse<String> response = request.asString();
		return Double.valueOf(response.getBody());
	}

	public Invoice[] getInvoices(String username) throws UnirestException {
		GetRequest request = Unirest.get(url + "store/purchase/{username}/unclaimed");
		request.header("key", apiKey);
		request.routeParam("username", username);

		HttpResponse<String> response = request.asString();
		return GSON.fromJson(response.getBody(), Invoice[].class);
	}

	public String redeemInvoice(String username, int invoiceId) throws UnirestException {
		HttpRequestWithBody request = Unirest.post(url + "store/purchase/{username}/redeem/{invoiceId}");
		request.header("key", apiKey);
		request.routeParam("username", username);
		request.routeParam("invoiceId", Integer.toString(invoiceId));

		HttpResponse<String> response = request.asString();
		return response.getBody();
	}

	public int unclaimedVotes(String username) throws UnirestException {
		HttpRequestWithBody request = Unirest.post(url + "vote/{username}");
		request.header("key", apiKey);
		request.routeParam("username", username);

		HttpResponse<String> response = request.asString();
		return Integer.parseInt(response.getBody());
	}
}
