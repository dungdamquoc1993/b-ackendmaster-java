/**
 * Archie
 */
package com.vinplay.item;

import java.io.Serializable;


/**
 * @author Archie
 *
 */
public class EsportRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5097540192133227674L;
	private String id;
	private String loginname;
	private String bet_selection;
	private Double odds;
	private String currency;
	private Double amount;
	private String game_type_name;
	private String game_market_name;
	private String market_option;
	private String map_num;// null,
	private String bet_type_name;// "WIN",
	private String competition_name;// "HomeSweetHome",
	private Long event_id;// 25032,
	private String event_name;// "Gambit Youngsters vs Copenhagen Flames",
	private String event_datetime;// "2020-04-17T09:50:51Z",
	private String date_created;// "2020-04-17T09:26:34.001085Z",
	private String settlement_datetime;// "2020-04-22T03:57:31.917363Z",
	private String modified_datetime;// "2020-04-22T03:57:31.944164Z",
	private String settlement_status;// "cancelled",
	private String result;// null,
	private String result_status;// "CANCELLED",
	private Double earnings;// null,
	private Double handicap;// null,
	private String is_combo;// false,
	private String member_code;// "test22",
	private String is_unsettled;// false,
	private String ticket_type;// "db",
	private Double malay_odds;// -0.64,
	private Double euro_odds;// 2.56,
	private Double member_odds;// 2.56,
	private String member_odds_style;// "euro",
	private Integer game_type_id;// 1,
	private String request_source;// "desktop-browser"
	
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBet_selection() {
		return bet_selection;
	}

	public void setBet_selection(String bet_selection) {
		this.bet_selection = bet_selection;
	}

	public Double getOdds() {
		return odds;
	}

	public void setOdds(Double odds) {
		this.odds = odds;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getGame_type_name() {
		return game_type_name;
	}

	public void setGame_type_name(String game_type_name) {
		this.game_type_name = game_type_name;
	}

	public String getGame_market_name() {
		return game_market_name;
	}

	public void setGame_market_name(String game_market_name) {
		this.game_market_name = game_market_name;
	}

	public String getMarket_option() {
		return market_option;
	}

	public void setMarket_option(String market_option) {
		this.market_option = market_option;
	}

	public String getMap_num() {
		return map_num;
	}

	public void setMap_num(String map_num) {
		this.map_num = map_num;
	}

	public String getBet_type_name() {
		return bet_type_name;
	}

	public void setBet_type_name(String bet_type_name) {
		this.bet_type_name = bet_type_name;
	}

	public String getCompetition_name() {
		return competition_name;
	}

	public void setCompetition_name(String competition_name) {
		this.competition_name = competition_name;
	}

	public Long getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getEvent_datetime() {
		return event_datetime;
	}

	public void setEvent_datetime(String event_datetime) {
		this.event_datetime = event_datetime;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public String getSettlement_datetime() {
		return settlement_datetime;
	}

	public void setSettlement_datetime(String settlement_datetime) {
		this.settlement_datetime = settlement_datetime;
	}

	public String getModified_datetime() {
		return modified_datetime;
	}

	public void setModified_datetime(String modified_datetime) {
		this.modified_datetime = modified_datetime;
	}

	public String getSettlement_status() {
		return settlement_status;
	}

	public void setSettlement_status(String settlement_status) {
		this.settlement_status = settlement_status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult_status() {
		return result_status;
	}

	public void setResult_status(String result_status) {
		this.result_status = result_status;
	}

	public Double getEarnings() {
		return earnings;
	}

	public void setEarnings(Double earnings) {
		this.earnings = earnings;
	}

	public Double getHandicap() {
		return handicap;
	}

	public void setHandicap(Double handicap) {
		this.handicap = handicap;
	}

	public String getIs_combo() {
		return is_combo;
	}

	public void setIs_combo(String is_combo) {
		this.is_combo = is_combo;
	}

	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

	public String getIs_unsettled() {
		return is_unsettled;
	}

	public void setIs_unsettled(String is_unsettled) {
		this.is_unsettled = is_unsettled;
	}

	public String getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(String ticket_type) {
		this.ticket_type = ticket_type;
	}

	public Double getMalay_odds() {
		return malay_odds;
	}

	public void setMalay_odds(Double malay_odds) {
		this.malay_odds = malay_odds;
	}

	public Double getEuro_odds() {
		return euro_odds;
	}

	public void setEuro_odds(Double euro_odds) {
		this.euro_odds = euro_odds;
	}

	public Double getMember_odds() {
		return member_odds;
	}

	public void setMember_odds(Double member_odds) {
		this.member_odds = member_odds;
	}

	public String getMember_odds_style() {
		return member_odds_style;
	}

	public void setMember_odds_style(String member_odds_style) {
		this.member_odds_style = member_odds_style;
	}

	public Integer getGame_type_id() {
		return game_type_id;
	}

	public void setGame_type_id(Integer game_type_id) {
		this.game_type_id = game_type_id;
	}

	public String getRequest_source() {
		return request_source;
	}

	public void setRequest_source(String request_source) {
		this.request_source = request_source;
	}

	public EsportRecord(String id, String bet_selection, Double odds, String currency, Double amount,
			String game_type_name, String game_market_name, String market_option, String map_num, String bet_type_name,
			String competition_name, Long event_id, String event_name, String event_datetime, String date_created,
			String settlement_datetime, String modified_datetime, String settlement_status, String result,
			String result_status, Double earnings, Double handicap, String is_combo, String member_code,
			String is_unsettled, String ticket_type, Double malay_odds, Double euro_odds, Double member_odds,
			String member_odds_style, Integer game_type_id, String request_source) {
		this.id = id;
		this.bet_selection = bet_selection;
		this.odds = odds;
		this.currency = currency;
		this.amount = amount;
		this.game_type_name = game_type_name;
		this.game_market_name = game_market_name;
		this.market_option = market_option;
		this.map_num = map_num;
		this.bet_type_name = bet_type_name;
		this.competition_name = competition_name;
		this.event_id = event_id;
		this.event_name = event_name;
		this.event_datetime = event_datetime;
		this.date_created = date_created;
		this.settlement_datetime = settlement_datetime;
		this.modified_datetime = modified_datetime;
		this.settlement_status = settlement_status;
		this.result = result;
		this.result_status = result_status;
		this.earnings = earnings;
		this.handicap = handicap;
		this.is_combo = is_combo;
		this.member_code = member_code;
		this.is_unsettled = is_unsettled;
		this.ticket_type = ticket_type;
		this.malay_odds = malay_odds;
		this.euro_odds = euro_odds;
		this.member_odds = member_odds;
		this.member_odds_style = member_odds_style;
		this.game_type_id = game_type_id;
		this.request_source = request_source;
	}

	public EsportRecord() {
		super();
	}

}
