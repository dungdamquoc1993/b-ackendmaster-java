package game.Jetty.model;

public class FundData {
	public long[] listFund;

	public FundData() {
	}

	public FundData(long fund) {
		this.listFund = new long[] { fund };
	}

	public FundData(long[] fund) {
		this.listFund = fund;
	}

}
