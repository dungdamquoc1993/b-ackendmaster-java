/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.vbee.common.enums;

public enum Games {
    MINIGAME(0, "MiniGame", "Minigame"),
    MINI_POKER(1, "MiniPoker", "Mini poker"),
    TAI_XIU(2, "TaiXiu", "T\u00e0i x\u1ec9u"),
    TAI_XIU_MD5(2, "TaiXiuMd5", "T\u00e0i x\u1ec9u Md5"),
    BAU_CUA(3, "BauCua", "B\u1ea7u cua"),
    CAO_THAP(4, "CaoThap", "Cao th\u1ea5p"),
    POKE_GO(5, "PokeGo", "Pokego"),
    CANDY(5, "CANDY", "CANDY"),
    POKEMON(6, "candy", "candy"),
    VQMM(7, "VQMM", "V\u00f2ng quay may m\u1eafn"),
    SAM(8, "Sam", "S\u00e2m - S\u00e2m (Solo)"),
    BA_CAY(9, "BaCay", "Ba c\u00e2y"),
    BINH(10, "Binh", "M\u1eadu binh"),
    TLMN(11, "Tlmn", "TLMN - TLMN (Solo)"),
    TA_LA(12, "TaLa", "T\u00e1 l\u1ea3"),
    LIENG(13, "Lieng", "Li\u00eang"),
    XI_TO(14, "XiTo", "X\u00ec t\u1ed1"),
    XOC_DIA(15, "XocDia", "X\u00f3c \u0111\u0129a"),
    BAI_CAO(16, "BaiCao", "B\u00e0i c\u00e0o"),
    POKER(17, "Poker", "Poker"),
    AVENGERS(18, "SieuAnhHung", "Si\u00eau anh h\u00f9ng"),
    MY_NHAN_NGU(19, "MyNhanNgu", "M\u1ef9 nh\u00e2n ng\u01b0"),
    KHO_BAU(20, "KhoBau", "Kho b\u00e1u"),
    NU_DIEP_VIEN(21, "NuDiepVien", "N\u1eef \u0111i\u1ec7p vi\u00ean"),
    VUONG_QUOC_VIN(22, "VuongQuocVin", "\u0054\u0068\u1ed5 \u0044\u00e2\u006e"),
    XI_DZACH(23, "XiDzach", "X\u00ec D\u00e1ch"),
    CARO(25, "Caro", "C\u1edd Caro"),
    CO_TUONG(26, "CoTuong", "C\u1edd T\u01b0\u1edbng"),
    CO_VUA(27, "CoVua", "C\u1edd Vua"),
    POKER_TOUR(28, "PokerTour", "Poker Tour"),
    CO_UP(29, "CoUp", "C\u1edd \u00dap"),
    HAM_CA_MAP(30, "HamCaMap", "H\u00e0m C\u00e1 M\u1eadp"),


    OVER_UNDER(102, "OverUnder", "Over-Under"),
    SPARTAN(120,"Spartan","Spartan"),
    AUDITION(110,"Audition","Audition"),
    SAMTRUYEN(130,"SamTruyen","SamTruyen"),
    RANGE_ROVER(140,"RANGE_ROVER","RANGE_ROVER"),
    MAYBACH(150,"MAYBACH","MAYBACH"),
    TAMHUNG(160,"TAMHUNG","TAMHUNG"),
    BENLEY(170,"BENLEY","BENLEY"),
    ROLL_ROYE(180,"ROLL_ROYE","ROLL_ROYE"),
    NHIEM_VU(181,"NHIEM_VU","NHIEM_VU"),
    GIFT_CODE(182,"GIFT_CODE","GIFT_CODE"),

    AG_GAMES(183,"AG_GAMES","AG_GAMES"),
    WM_GAMES(184,"WM_GAMES","WM_GAMES"),
    IBC2_GAMES(185,"IBC2_GAMES","IBC2_GAMES"),
    
    
    HOAN_TRA(186,"HOAN_TRA","HOAN_TRA"),
    KHUYEN_MAI(187,"KHUYEN_MAI","KHUYEN_MAI"),
    VERIFY_PHONE(188,"VERIFY_PHONE","VERIFY_PHONE"),
    
    CMD_GAMES(189,"CMD_GAMES","CMD_GAMES"),
	TAI_XIU_ST(190, "TaiXiuST", "T\u00e0i x\u1ec9u ST"),
	CHIEM_TINH(191,"CHIEMTINH","CHIEMTINH"),
	SHOT_FISH(192,"FISH","FISH"),
	MOON_NIGHT(193,"MOON_NIGHT","MOON_NIGHT"),
	
	EBET_GAMES(194, "EBET_GAMES", "EBET_GAMES"), 
	SBO_GAMES(195, "SBO_GAMES", "SBO_GAMES"),
    SEXYGIRL(196,"SEXYGIRL","SEXYGIRL"),
    BIKINI(197,"BIKINI","BIKINI"),
    GALAXY(198,"GALAXY","GALAXY"),
    DIEM_DANH(199,"DIEM_DANH","DIEM_DANH"),
	AGENT_TOPUP(200,"AGENT_TOPUP","AGENT_TOPUP"),
	LUCKY_MONEY(201,"LUCKY_MONEY","LUCKY_MONEY"),
	USER_WAGES(202,"USER_WAGES","USER_WAGES");


    private int id;
    private String name;
    private String description;

    private Games(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Games findGameById(int id) {
        for (Games entry : Games.values()) {
            if (entry.getId() != id) continue;
            return entry;
        }
        return null;
    }

	public static String findTableNameById(int id) {
		for (Games entry : Games.values()) {
			if (entry.getId() == id) {
				return "log_" + entry.getName();
			}
		}
		return "";
	}
	
	public static String getGameNameById(int id) {
		for (Games entry : Games.values()) {
			if (entry.getId() == id) {
				return entry.getName();
			}
		}
		return "";
	}

    public static Games findGameByName(String name) {
        for (Games entry : Games.values()) {
            if (!entry.getName().equalsIgnoreCase(name)) continue;
            return entry;
        }
        return null;
    }
}

