package game.tienlen.server.BotTlmn;

import bitzero.util.common.business.Debug;

import java.util.*;

/**
 * Created by hp on 9/17/2019.
 */
public class BotAttackInGame {

    public static byte[] getListCardToBy(ArrayList<Card> arrayList) {
        byte[] listCard = new byte[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            Card card = arrayList.get(i);
            listCard[i] = card.id;
        }
        return listCard;
    }

    public static byte[] botAttackInGame(Hand hand, byte[] currentCard, boolean isFirstPlay) {
        return botAttackInGame(hand, currentCard, isFirstPlay, false,false);
    }

    public static byte[] getCardAttackFirstPriority(SortCardData sortCardData, boolean attackMax){
        int[] checkPointStraightLenght = new int[13];
        int sizeCheckPoint = -1;
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.STRAIGH_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.STRAIGH_CARD);
            for(int i =0;i<arrayList.size();i++){
                ArrayList<Card> listCardAttack = arrayList.get(i);
                if(checkPointStraightLenght[listCardAttack.size()]>0){
                    sizeCheckPoint =listCardAttack.size();
                    break;
                }else{
                    checkPointStraightLenght[listCardAttack.size()]=1;
                }
            }
            if(sizeCheckPoint>0){
                for(int i =0;i<arrayList.size();i++) {
                    ArrayList<Card> listCardAttack = arrayList.get(i);
                    if(listCardAttack.size() ==  sizeCheckPoint){
                        return getListCardToBy(listCardAttack);
                    }
                }
            }
        }


        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);

            //sam tu cao xuong thap
            if(arrayList.size()>1){
                return getListCardToBy(arrayList.get(arrayList.size() - 1));
            }
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.PAIR_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.PAIR_CARD);
            // doi tu cao xuong thap
            if(arrayList.size()>1){
                return getListCardToBy(arrayList.get(arrayList.size() - 1));
            }
        }
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.ONE_CARD)){
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.ONE_CARD);

            if(!attackMax && arrayList.size()>1 && sortCardData.mapTypeOfAllCard.size()>1){

                if(arrayList.get(arrayList.size()-1).number>11){
                    ArrayList<Card> arrayList1 = new ArrayList<>();
                    arrayList1.add(arrayList.get(0));
                    return getListCardToBy(arrayList1);
                }
                if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.PAIR_CARD)) {
                    ArrayList<ArrayList<Card>> arrayListPairCard = (ArrayList<ArrayList<Card>>)
                            sortCardData.mapTypeOfAllCard.get(GroupCard.PAIR_CARD);
                    if(arrayListPairCard.get(0).get(0).number>11){
                        ArrayList<Card> arrayList1 = new ArrayList<>();
                        arrayList1.add(arrayList.get(0));
                        return getListCardToBy(arrayList1);
                    }
                }
                if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)) {
                    ArrayList<ArrayList<Card>> arrayListThreeOfAKindCard = (ArrayList<ArrayList<Card>>)
                            sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);
                    if(arrayListThreeOfAKindCard.get(0).get(0).number>11){
                        ArrayList<Card> arrayList1 = new ArrayList<>();
                        arrayList1.add(arrayList.get(0));
                        return getListCardToBy(arrayList1);
                    }
                }
            }
        }
        return null;
    }

    public static byte[] getListCardSortCard2(SortCardData sortCardData){
        // check co bo dac biet khong va tong so bo la bao nhieu, neu co bo dac biet va tong so bo = 2 thi danh bo dac biet
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_PAIR_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_PAIR_CARD);
            return getListCardToBy(arrayList);
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.FOUR_OF_A_KIND_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.FOUR_OF_A_KIND_CARD);
            //tu quy tu cao xuong thap
            return getListCardToBy(arrayList.get(arrayList.size() - 1));
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.FOUR_PAIR_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.FOUR_PAIR_CARD);
            return getListCardToBy(arrayList);
        }
        return null;
    }

    public static byte[] botAttackFirst(SortCardData sortCardData, boolean attackMax){
        if (sortCardData.numberBo <= 2) {
           byte[] listCardSortCard2 = getListCardSortCard2(sortCardData);
           if(listCardSortCard2!=null){
               Debug.trace("getListCardSortCard2");
               return listCardSortCard2;
           }
        }

        byte[] listCardPrio = getCardAttackFirstPriority(sortCardData, attackMax);
        if(listCardPrio!=null){
            Debug.trace("getCardAttackFirstPriority");
            return listCardPrio;
        }


        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.STRAIGH_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.STRAIGH_CARD);
            // sanh tu thap len cao
            //can phai len lai rule dang lay sanh be nhat
            return getListCardToBy(arrayList.get(0));



        }
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);

            //sam tu cao xuong thap
            return getListCardToBy(arrayList.get(arrayList.size() - 1));
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.PAIR_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.PAIR_CARD);

            // doi tu cao xuong thap


            return getListCardToBy(arrayList.get(arrayList.size() - 1));
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.ONE_CARD)) {
            if(attackMax){
                byte maxiumNumber = -1;
                for(int i =0;i<sortCardData.listCard.size();i++){
                    Card card = sortCardData.listCard.get(i);
                    if(card.id>maxiumNumber){
                        maxiumNumber = card.id;
                    }
                }
                byte[] cardCanAttack = new byte[]{maxiumNumber};
                return cardCanAttack;

            }
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.ONE_CARD);

            // tu thap len cao
            ArrayList<Card> arrayList1 = new ArrayList<>();
            arrayList1.add(arrayList.get(0));
            return getListCardToBy(arrayList1);
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_PAIR_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_PAIR_CARD);
            return getListCardToBy(arrayList);
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.FOUR_OF_A_KIND_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.FOUR_OF_A_KIND_CARD);

            //tu quy tu cao xuong thap
            return getListCardToBy(arrayList.get(arrayList.size() - 1));
        }

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.FOUR_PAIR_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.FOUR_PAIR_CARD);
            return getListCardToBy(arrayList);
        }
        return null;
    }

    public static byte[] botAttackSpecial(SortCardData sortCardData, GroupCard groupCard){

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_PAIR_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_PAIR_CARD);
            byte[] allCard = getListCardToBy(arrayList);
            GroupCard groupCard3 = new GroupCard(allCard);
            if (CompareCardTLMN.canAttack(groupCard, groupCard3)) {
                return allCard;
            }
        }
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.FOUR_OF_A_KIND_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.FOUR_OF_A_KIND_CARD);
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                ArrayList<Card> arrayListTuQuy = arrayList.get(i);
                byte[] allCard = getListCardToBy(arrayListTuQuy);
                GroupCard groupCard3 = new GroupCard(allCard);
                if (CompareCardTLMN.canAttack(groupCard, groupCard3)) {
                    return allCard;
                }
            }
        }
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.FOUR_PAIR_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.FOUR_PAIR_CARD);
            byte[] allCard = getListCardToBy(arrayList);
            GroupCard groupCard3 = new GroupCard(allCard);
            if (CompareCardTLMN.canAttack(groupCard, groupCard3)) {
                return allCard;
            }
        }
        return null;
    }

    public static byte[] botAttackInGame(byte[] playerCard, byte[] currentCard, boolean isFirstPlay,
                                         boolean mustAttack, boolean attackMax){ // su dung function nay de danh bai
        // mustAttack: bo luot qua 2 turn thi neu danh duoc se bat buoc danh.
        // attackMax: Co 1 nguoi choi con 1 la, va chua bo luot
        Hand hand = new Hand(playerCard);
        return botAttackInGame(hand, currentCard, isFirstPlay, mustAttack, attackMax);

    }
    public static byte[] botAttackInGame(Hand hand, byte[] currentCard, boolean isFirstPlay,
                                         boolean mustAttack, boolean attackMax) {
        SortCardData sortCardData = sortCard(hand);
        if (isFirstPlay) {
            return botAttackFirst(sortCardData,attackMax);
        } else {
            //tim tat ca cac bo co the danh duoc, neu khong danh duoc tra ve null
            GroupCard groupCard = new GroupCard(currentCard);
            if (groupCard.strong > 0) {
//                //cho nay check cac bo dac biet
                byte[] botAttackSpecial = botAttackSpecial(sortCardData,groupCard);
                if(botAttackSpecial!=null){
                    return botAttackSpecial;
                }
            }

            if (groupCard.type == GroupCard.STRAIGH_CARD) {
                return getSortCardStraightCard(sortCardData,groupCard,mustAttack||attackMax);
            }

            if (groupCard.type == GroupCard.PAIR_CARD) {
                return getSortCardPairCard(sortCardData,groupCard,mustAttack||attackMax);

            }

            if (groupCard.type == GroupCard.THREE_OF_A_KIND_CARD) {
               return getSortCardThreeOfAKindCard(sortCardData,groupCard,mustAttack||attackMax);
            }

            if (groupCard.type == GroupCard.ONE_CARD) {
                return getCardOneCard(sortCardData,groupCard,mustAttack, attackMax);

            }
        }
        return null;
    }

    public static byte[] getSortCardStraightCard(SortCardData sortCardData, GroupCard groupCard, boolean mustAttack){
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.STRAIGH_CARD)) {
            int lengthStraight = groupCard.cards.size();
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.STRAIGH_CARD);
            //tim sanh co the danh duoc
            for (int i = 0; i < arrayList.size(); i++) {
                ArrayList<Card> arrayListSanh = arrayList.get(i);
                if (arrayListSanh.size() == lengthStraight) {
                    byte[] listCardSanh = getListCardToBy(arrayListSanh);
                    GroupCard groupCardSanh = new GroupCard(listCardSanh);
                    if (CompareCardTLMN.canAttack(groupCard, groupCardSanh)) {
                        return listCardSanh;
                    }
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                ArrayList<Card> arrayListSanh = arrayList.get(i);
                if (arrayListSanh.size() >= lengthStraight) {
                    int delta = arrayListSanh.size() - lengthStraight;
                    Card maxiumCardStraight = groupCard.cards.get(groupCard.cards.size() - 1);

                    int miniumCardOne = 13; // bang tong so cay toi da, nen se tim dc min
                    ArrayList<Card> arrayListReturn = new ArrayList<>();
                    for (int count = 0; count < delta; count++) {
                        int currentIndex = arrayListSanh.size() - 1 - count;
                        Card cardArraySanh = arrayListSanh.get(currentIndex);
                        if (cardArraySanh.id > maxiumCardStraight.id) {
                            ArrayList<Card> listCard = new ArrayList<>(sortCardData.listCard);
                            ArrayList<Card> listCardReturn = new ArrayList<>();
                            for (int countData = currentIndex; countData > currentIndex - lengthStraight; countData--) {
                                Card cardAdd = arrayListSanh.get(countData);
                                listCard.remove(cardAdd);
                                listCardReturn.add(cardAdd);
                            }
                            byte[] currentCardCanAttack = getListCardToBy(listCard);
                            Hand handSortCardSanh = new Hand(currentCardCanAttack);
                            SortCardData sortCardDataSanh = sortCard(handSortCardSanh);
                            if (sortCardDataSanh.numberCardOne < miniumCardOne) {
                                miniumCardOne = sortCardDataSanh.numberCardOne;
                                arrayListReturn = listCardReturn;
                            }
                        } else {
                            break;
                        }
                    }
                    if (arrayListReturn.size() > 0) {
                        if (miniumCardOne <= sortCardData.numberCardOne+1) {
                            return getListCardToBy(arrayListReturn);
                        }
                        if (mustAttack) {
                            return getListCardToBy(arrayListReturn);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static byte[] getSortCardThreeOfAKindCard(SortCardData sortCardData, GroupCard groupCard, boolean mustAttack){
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);
            //cac bo trong nay tu cao xuong thap
            for (int i = arrayList.size() - 1; i >= 0; i--) {
                ArrayList<Card> arrayListSam = arrayList.get(i);
                if (arrayListSam.get(0).number == 12) continue; // 3 con hai thi k chan nhe
                byte[] listCardSam = getListCardToBy(arrayListSam);
                GroupCard groupCardSam = new GroupCard(listCardSam);
                if (CompareCardTLMN.canAttack(groupCard, groupCardSam)) {
                    return listCardSam;
                }
            }

        }
        return null;
    }

    public static byte[] getSortCardPairCard(SortCardData sortCardData, GroupCard groupCard, boolean mustAttack){
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.PAIR_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.PAIR_CARD);                    //cac bo trong nay tu cao xuong thap

            for (int i = arrayList.size() - 1; i >= 0; i--) {
                ArrayList<Card> arrayListdoi = arrayList.get(i);
                byte[] listCardDoi = getListCardToBy(arrayListdoi);
                GroupCard groupCardDoi = new GroupCard(listCardDoi);
                if (CompareCardTLMN.canAttack(groupCard, groupCardDoi)) {
                    return listCardDoi;
                }
            }

            if (mustAttack) {
                if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)) {
                    ArrayList<ArrayList<Card>> allListCardSam = (ArrayList<ArrayList<Card>>)
                            sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);
                    for (int i = allListCardSam.size() - 1; i >= 0; i--) {
                        ArrayList<Card> arrayListSam = arrayList.get(i);
                        byte[] listCardAttack = new byte[]{arrayListSam.get(0).number,
                                arrayListSam.get(1).number};
                        GroupCard groupCardAtt = new GroupCard(listCardAttack);
                        if (CompareCardTLMN.canAttack(groupCard, groupCardAtt)) {
                            return listCardAttack;
                        }
                    }
                }
            }
//                        //cac bo trong nay tu cao xuong thap

        }
        return null;
    }

    public static byte[] getCardOneCard(SortCardData sortCardData, GroupCard groupCard, boolean mustAttack, boolean attackMax){

        if(attackMax){
            byte maxiumNumber = -1;
            for(int i =0;i<sortCardData.listCard.size();i++){
                Card card = sortCardData.listCard.get(i);
                if(card.id>maxiumNumber){
                    maxiumNumber = card.id;
                }
            }
            byte[] cardCanAttack = new byte[]{maxiumNumber};
            GroupCard groupCardAttack = new GroupCard(cardCanAttack);
            if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                return cardCanAttack;
            }
        }
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.ONE_CARD)) {
            ArrayList<Card> arrayList = (ArrayList<Card>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.ONE_CARD);
            for (int i = 0; i < arrayList.size(); i++) {
                Card card = arrayList.get(i);
                byte[] cardCanAttack = new byte[]{card.id};
                GroupCard groupCardAttack = new GroupCard(cardCanAttack);
                if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                    return cardCanAttack;
                }
            }
        }
        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.PAIR_CARD)) {
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.PAIR_CARD);
            ArrayList<Card> listCardHai = arrayList.get(0);
            if(listCardHai.get(0).number == 12){
                for(int i  =0;i<listCardHai.size();i++){
                    Card card = listCardHai.get(i);
                    byte[] cardCanAttack = new byte[]{card.id};
                    GroupCard groupCardAttack = new GroupCard(cardCanAttack);
                    if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                        return cardCanAttack;
                    }
                }
            }
        }                    // co the pha cac bo ra, pha sanh cao nhat ra truoc xong den doi cao nhat

        if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)){ // pha ba con hai ra
            ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                    sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);
            ArrayList<Card> listCardHai = arrayList.get(0);
            if(listCardHai.get(0).number == 12){
                for(int i  =0;i<listCardHai.size();i++){
                    Card card = listCardHai.get(i);
                    byte[] cardCanAttack = new byte[]{card.id};
                    GroupCard groupCardAttack = new GroupCard(cardCanAttack);
                    if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                        return cardCanAttack;
                    }
                }
            }
        }
        if(mustAttack){
            if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.THREE_OF_A_KIND_CARD)) { // pha bo ba ra
                ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                        sortCardData.mapTypeOfAllCard.get(GroupCard.THREE_OF_A_KIND_CARD);
                for(int i = arrayList.size() -1;i>=0;i--){
                    ArrayList<Card> listCardAttack = arrayList.get(i);
                    for(int count = 0;count<listCardAttack.size();count++){
                        Card card = listCardAttack.get(count);
                        byte[] cardCanAttack = new byte[]{card.id};
                        GroupCard groupCardAttack = new GroupCard(cardCanAttack);
                        if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                            return cardCanAttack;
                        }
                    }
                }
            }
            if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.PAIR_CARD)) {  // pha doi ra
                ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                        sortCardData.mapTypeOfAllCard.get(GroupCard.PAIR_CARD);
                for(int i =0;i<arrayList.size();i++){
                    ArrayList<Card> listCardAttack = arrayList.get(i);
                    for(int count = 0;count<listCardAttack.size();count++){
                        Card card = listCardAttack.get(count);
                        byte[] cardCanAttack = new byte[]{card.id};
                        GroupCard groupCardAttack = new GroupCard(cardCanAttack);
                        if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                            return cardCanAttack;
                        }
                    }
                }
            }

            if (sortCardData.mapTypeOfAllCard.containsKey(GroupCard.STRAIGH_CARD)) {  //lay 1 quan sanh ra
                ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>)
                        sortCardData.mapTypeOfAllCard.get(GroupCard.STRAIGH_CARD);
                for(int i =0;i<arrayList.size();i++){
                    ArrayList<Card> listCardAttack = arrayList.get(i);
                    if(listCardAttack.size()>3){
                        Card card = listCardAttack.get(listCardAttack.size()-1);
                        byte[] cardCanAttack = new byte[]{card.id};
                        GroupCard groupCardAttack = new GroupCard(cardCanAttack);
                        if (CompareCardTLMN.canAttack(groupCard, groupCardAttack)) {
                            return cardCanAttack;
                        }
                    }

                }
            }
        }
        return null;
    }

    // test sort card
    public static void main(String[] args) {
        byte[] cardBot = new byte[]{16, 20, 24, 25, 28, 51, 45};
        Hand hand = new Hand(cardBot);
        byte[] listCardAttack = botAttackInGame(hand, new byte[]{0}, false);
        System.out.println("done main");
//        for (int count = 0; count < 100000; count++) {
//            List<List<Byte>> allCard = DealCard.dealCardNormal((byte) 4);
//            List listCard = allCard.get(0);
//            byte[] card = DealCard.changeListToArrayCard(listCard);
//            Hand hand = new Hand(card);
//            byte[] listCardAttack = botAttackInGame(hand, new byte[]{50}, false);
//            if (listCardAttack == null) continue;
//            for (int i = 0; i < listCardAttack.length; i++) {
//                System.out.print(listCardAttack[i] + ", ");
//            }
//            System.out.println();
////            if (listCardAttack.length == 6) break;
//            //break;
////            SortCardData sortCardData = sortCard(hand);
////            List<Card> listSortCard = sortCardData.listCard;
////            for (int i = 0; i < listSortCard.size(); i++) {
////                System.out.print(listSortCard.get(i).number + ", ");
////            }
//        }

    }


//    public static void main(String[] args) {
//
//    }

    public static SortCardData sortCard(Hand hand) {
        byte[] checkPoint = new byte[13];

        for (int i = 0; i < hand.cards.size(); i++) {
            checkPoint[hand.cards.get(i).number]++;
        }
        List<Card> cardDeck = new ArrayList<>(hand.cards);
        List<Card> cardReturn = new ArrayList<>();


        Map<Integer, Object> mapTypeOfAllCard = new HashMap<>();// se push vao cai map nay
        int numberbo = 0;

        int indexBonDt = checkBonDoiThong(checkPoint);
        if (indexBonDt > -1) {
            //co bon doi thong thi khong can check ba doi thong voi tu quy nua
            getCardDoiThong(indexBonDt, checkPoint, cardDeck, cardReturn, 8, mapTypeOfAllCard);
            numberbo++;

        }

        List<Integer> indexTq = checkTuQuy(checkPoint);
        if (indexTq.size() > 0) {
            for (int i = 0; i < indexTq.size(); i++) {
                getCardTuQuySamDoi(indexTq.get(i), checkPoint, cardDeck, cardReturn, mapTypeOfAllCard);
                numberbo++;
            }
        }
        int indexBaDt = checkBaDoiThong(checkPoint);
        if (indexBaDt > -1) {
            // System.out.println("co ba doi thong");
            getCardDoiThong(indexBaDt, checkPoint, cardDeck, cardReturn, 6, mapTypeOfAllCard);
            numberbo++;
        }


//        System.out.println("check point");
//        for (int i = 0; i < checkPoint.length; i++) {
//            System.out.print(checkPoint[i] + ", ");
//        }
//        System.out.println();

        while (true) {
            int[] index = getSanhDaiNhat(checkPoint);
            if (index != null) {
//                System.out.println("chec sanh dai   " + index[0] + "    " + index[1]);
                getSanhDaiNhat(index[0], index[1], checkPoint, cardDeck, cardReturn, mapTypeOfAllCard);
                numberbo++;
            } else {
                break;
            }
        }

        List<Integer> indexSam = checkSam(checkPoint);
        if (indexSam.size() > 0) {
//            System.out.println("check sam  " + indexSam.size());
            for (int i = 0; i < indexSam.size(); i++) {
                getCardTuQuySamDoi(indexSam.get(i), checkPoint, cardDeck, cardReturn, mapTypeOfAllCard);
                numberbo++;
            }
        }

        List<Integer> indexDoi = checkDoi(checkPoint);
        if (indexDoi.size() > 0) {
            for (int i = 0; i < indexDoi.size(); i++) {
                getCardTuQuySamDoi(indexDoi.get(i), checkPoint, cardDeck, cardReturn, mapTypeOfAllCard);
                numberbo++;
            }
        }

//        System.out.println("check point");
//        for (int i = 0; i < checkPoint.length; i++) {
//            System.out.print(checkPoint[i] + ", ");
//        }
//        System.out.println();

//        int le = cardDeck.size();
//        System.out.println("le   " + le);
        Iterator it = cardDeck.iterator();
        ArrayList<Card> listCardLe = new ArrayList<>();
        int numberCardLe = 0;
        while (it.hasNext()) {
            Card card = (Card) it.next();
            cardReturn.add(card);
            it.remove();
            listCardLe.add(card);
            numberCardLe++;
            numberbo++;
        }

        mapTypeOfAllCard.put(GroupCard.ONE_CARD, listCardLe);

        SortCardData sortCardData = new SortCardData(cardReturn, mapTypeOfAllCard, numberCardLe, numberbo);

        return sortCardData;
    }

    //    public static void getCardSam(int index, byte[] checkPoint, List<Card> cardDeck, List<Card> cardReturn){
//
//    }
    public static void getSanhDaiNhat(int indexStart, int length, byte[] checkPoint, List<Card> cardDeck,
                                      List<Card> cardReturn, Map<Integer, Object> mapTypeOfAllCard) {
        int currentIndex = indexStart - length + 1;
        int currentLenght = length;


        ArrayList<Card> listCard = new ArrayList<>();

        Iterator it = cardDeck.iterator();
        byte checkpointIndex = checkPoint[currentIndex];
        while (it.hasNext()) {
            Card card = (Card) it.next();
            if (card.number == currentIndex) {
                if (checkpointIndex > 1) {
                    checkpointIndex--;
                } else {
                    // System.out.println("add Card ");
                    it.remove();
                    cardReturn.add(card);
                    checkPoint[card.number]--;
                    currentIndex++;
                    currentLenght--;
                    listCard.add(card);
                }
            }
            if (currentLenght == 0) {
                if (!mapTypeOfAllCard.containsKey(GroupCard.STRAIGH_CARD)) {
                    ArrayList<ArrayList<Card>> arrayList = new ArrayList<>();
                    arrayList.add(listCard);
                    mapTypeOfAllCard.put(GroupCard.STRAIGH_CARD, arrayList);
                } else {
                    ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>) mapTypeOfAllCard.get(GroupCard.STRAIGH_CARD);
                    arrayList.add(listCard);
                    mapTypeOfAllCard.put(GroupCard.STRAIGH_CARD, arrayList);
                }
                return;
            }
        }

    }

    public static void getCardDoiThong(int index, byte[] checkPoint, List<Card> cardDeck,
                                       List<Card> cardReturn, int maxiumCard, Map<Integer, Object> mapTypeOfAllCard) {
        int numberCardadd = 0;
        ArrayList<Card> listCard = new ArrayList<>();
        for (int i = cardDeck.size() - 1; i >= 0; i--) {
            Card card = cardDeck.get(i);
            if (card.number == index - numberCardadd / 2) {
                cardDeck.remove(card);
                cardReturn.add(card);
                checkPoint[card.number]--;
                numberCardadd++;
                listCard.add(card);
            }
            if (numberCardadd == maxiumCard) {
                System.out.println("maxium card  " + maxiumCard);
                if (maxiumCard == 6) {
                    mapTypeOfAllCard.put(GroupCard.THREE_PAIR_CARD, listCard);
                }
                if (maxiumCard == 8) {
                    mapTypeOfAllCard.put(GroupCard.FOUR_PAIR_CARD, listCard);
                }
                return; // 3 doi thong la 6, bon doi thong la 8
            }
        }
    }

    public static void getCardTuQuySamDoi(int index, byte[] checkPoint, List<Card> cardDeck,
                                          List<Card> cardReturn, Map<Integer, Object> mapTypeOfAllCard) {
        Iterator it = cardDeck.iterator();
        int type = GroupCard.PAIR_CARD;
        if (checkPoint[index] == 3) {
            type = GroupCard.THREE_OF_A_KIND_CARD;
        }
        if (checkPoint[index] == 4) {
            type = GroupCard.FOUR_OF_A_KIND_CARD;
        }

        ArrayList<Card> listCard = new ArrayList<>();
        while (it.hasNext()) {
            Card card = (Card) it.next();
            if (card.number == index) {
                it.remove();
                cardReturn.add(card);
                checkPoint[index]--;
                listCard.add(card);
            }

            if (checkPoint[index] == 0) {
                if (!mapTypeOfAllCard.containsKey(type)) {
                    ArrayList<ArrayList<Card>> arrayList = new ArrayList<>();
                    arrayList.add(listCard);
                    mapTypeOfAllCard.put(type, arrayList);
                } else {
                    ArrayList<ArrayList<Card>> arrayList = (ArrayList<ArrayList<Card>>) mapTypeOfAllCard.get(type);
                    arrayList.add(listCard);
                    mapTypeOfAllCard.put(type, arrayList);
                }

            }

        }
    }

    public static int checkBonDoiThong(byte[] checkPoint) {
        for (int i = checkPoint.length - 2; i >= 3; i--) {// -2 vi khong tin con 2 vao ba doi thong
            if (checkPoint[i] > 1 && checkPoint[i - 1] > 1 && checkPoint[i - 2] > 1 && checkPoint[i - 3] > 1) {
                return i; // index cao nhat bon doi thong
            }
        }
        return -1;
    }

    public static List<Integer> checkTuQuy(byte[] checkPoint) {
        List<Integer> listTuQuy = new ArrayList<>();
        for (int i = checkPoint.length - 1; i >= 0; i--) {
            if (checkPoint[i] > 3) {
                listTuQuy.add(i); // index cao nhat tu quy
            }
        }
        return listTuQuy;
    }

    public static int checkBaDoiThong(byte[] checkPoint) {
        for (int i = checkPoint.length - 2; i >= 2; i--) {// -2 vi khong tin con 2 vao ba doi thong
            if (checkPoint[i] > 1 && checkPoint[i - 1] > 1 && checkPoint[i - 2] > 1) {
                return i; // index cao nhat ba doi thong
            }
        }
        return -1;
    }

    public static int[] getSanhDaiNhat(byte[] checkPoint) {
        for (int i = 0; i < checkPoint.length - 3; i++) {
            if (checkPoint[i] > 0 && checkPoint[i + 1] > 0 && checkPoint[i + 2] > 0) {
                for (int j = i + 2; j < checkPoint.length - 1; j++) {
                    if (checkPoint[j] < 1)
                        return new int[]{j - 1, j - i};  // tham so dau la index, tham so thu 2 la do dai cua sanh
                }
                return new int[]{checkPoint.length - 2,checkPoint.length - 1-i};
            }
        }
        return null;
    }

    public static List<Integer> checkSam(byte[] checkPoint) {
        List<Integer> listSam = new ArrayList<>();
        for (int i = checkPoint.length - 1; i >= 0; i--) {
            if (checkPoint[i] > 2) {
                listSam.add(i);
            }
        }
        return listSam;
    }

    public static List<Integer> checkDoi(byte[] checkPoint) {
        List<Integer> listDoi = new ArrayList<>();
        for (int i = checkPoint.length - 1; i >= 0; i--) {
            if (checkPoint[i] > 1) {
                listDoi.add(i);
            }
        }
        return listDoi;
    }

}
//
//    public static byte[][] getAllListCardCanAttack(Hand hand, byte[] currentCard) {
//        //tim tat ca cac bo danh duoc neu khong co tra ve null;
////        byte[] checkpoint = new byte[13];
////        for(Card card : hand.cards){
////            checkpoint[card.number]++;
////        }
//
//        GroupCard groupCard = new GroupCard(currentCard);
//        if (groupCard.strong > 0) {
//            switch (groupCard.strong) {
//                case GroupCard.ONE_2_STRONG: {
//                    getAllListCardCanAttackONE_2_STRONG(hand, groupCard);
//                    break;
//                }
//                case GroupCard.THREE_PAIR_CARD_SRONG: {
//                    getAllListCardCanAttackTHREE_PAIR_CARD_SRONG(hand, groupCard);
//                    break;
//                }
//                case GroupCard.PAIR_CARD_2_STRONG: {
//                    getAllListCardCanAttackPAIR_CARD_2_STRONG(hand, groupCard);
//                    break;
//                }
//                case GroupCard.FOUR_OF_A_KIND_CARD_STRONG: {
//                    getAllListCardCanAttackFOUR_OF_A_KIND_CARD_STRONG(hand, groupCard);
//                    break;
//                }
//                case GroupCard.FOUR_PAIR_CARD_STRONG: {
//                    getAllListCardCanAttackFOUR_PAIR_CARD_STRONG(hand, groupCard);
//                    break;
//                }
//            }
//
//        } else {
//            switch (groupCard.type) {
//                case GroupCard.ONE_CARD: {
//                    getAllListCardCanAttackONE_CARD(hand, groupCard);
//                    break;
//                }
//                case GroupCard.PAIR_CARD: {
//                    getAllListCardCanAttackPAIR_CARD(hand, groupCard);
//                    break;
//                }
//                case GroupCard.THREE_OF_A_KIND_CARD: {
//                    getAllListCardCanAttackTHREE_OF_A_KIND_CARD(hand, groupCard);
//                    break;
//                }
//                case GroupCard.STRAIGH_CARD: {
//                    getAllListCardCanAttackSTRAIGH_CARD(hand, groupCard);
//                    break;
//                }
//            }
//        }
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackONE_2_STRONG(Hand hand, GroupCard groupCard) {
//        Card cardMax = groupCard.cards.get(groupCard.cards.size() - 1);
//        ArrayList<ArrayList<Card>> listCardReturn = new ArrayList<>();
//        for (int i = hand.cards.size() - 1; i >= 0; i--) {
//            if (hand.cards.get(i).id > cardMax.id) {
//                ArrayList<Card> arrayList = new ArrayList<>();
//                arrayList.add(hand.cards.get(i));
//                listCardReturn.add(arrayList);
//            } else {
//                break;
//            }
//        }
//
//        byte[] checkPoint = new byte[13];
//        for (int i = 0; i < hand.cards.size(); i++) {
//            checkPoint[hand.cards.get(i).number]++;
//        }
//
//        byte[] threeStraighPair = getThreeStraighPair(hand, checkPoint);
//        if (threeStraighPair != null) {
//
//        }
//
//        byte[] fourOfAkind = getFourOfAkind(hand, checkPoint);
//        if (fourOfAkind != null) {
//
//        }
//
//        byte[] fourStraighPair = getFourStraighPair(hand, checkPoint);
//        if (fourStraighPair != null) {
//
//        }
//
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackTHREE_PAIR_CARD_SRONG(Hand hand, GroupCard groupCard) {
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackPAIR_CARD_2_STRONG(Hand hand, GroupCard groupCard) {
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackFOUR_OF_A_KIND_CARD_STRONG(Hand hand, GroupCard groupCard) {
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackFOUR_PAIR_CARD_STRONG(Hand hand, GroupCard groupCard) {
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackONE_CARD(Hand hand, GroupCard groupCard) {
//        Card cardMax = groupCard.cards.get(groupCard.cards.size() - 1);
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackPAIR_CARD(Hand hand, GroupCard groupCard) {
//        byte number = groupCard.cards.get(groupCard.cards.size() - 1).number;
//        byte maxiumCard = groupCard.cards.get(groupCard.cards.size() - 1).id;
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackTHREE_OF_A_KIND_CARD(Hand hand, GroupCard groupCard) {
//        byte number = groupCard.cards.get(0).number;
//
//        return null;
//    }
//
//    public static byte[][] getAllListCardCanAttackSTRAIGH_CARD(Hand hand, GroupCard groupCard) {
//        int sizeStraight = groupCard.cards.size();
//        byte maxiumIdStraigh = groupCard.cards.get(groupCard.cards.size() - 1).id;
//        return null;
//    }
//
//    public static byte[] getThreeStraighPair(Hand hand, byte[] checkPoint) {
//        for (int i = 0; i < checkPoint.length - 2; i++) {
//            if (checkPoint[i] > 1 && checkPoint[i + 1] > 1 && checkPoint[i + 2] > 1) {
//
//            }
//        }
//        return null;
//    }
//
//    public static byte[] getFourOfAkind(Hand hand, byte[] checkPoint) {
//        for (int i = 0; i < checkPoint.length; i++) {
//            if (checkPoint[i] == 4) {
//
//            }
//        }
//        return null;
//    }
//
//    public static byte[] getFourStraighPair(Hand hand, byte[] checkPoint) {
//        for (int i = 0; i < checkPoint.length - 3; i++) {
//            if (checkPoint[i] > 1 && checkPoint[i + 1] > 1 && checkPoint[i + 2] > 1 && checkPoint[i + 3] > 1) {
//
//            }
//        }
//        return null;
//    }
