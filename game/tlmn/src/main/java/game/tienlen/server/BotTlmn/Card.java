package game.tienlen.server.BotTlmn;

/**
 * Đại diện cho một quân bài
 */
public class Card {

	public byte number;
	public byte color;
	public byte id;

	public Card(byte id){
		this.id = id;
		this.color = (byte)(id%4);
		this.number = (byte)(id/4);
	}

	public boolean checkSameColorCard(Card c1){
		if(color ==0 || color == 1){
			return c1.color ==0||c1.color == 1;
		}
		return c1.color==2 || c1.color == 3;
	}
}
