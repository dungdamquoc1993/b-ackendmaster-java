package com.vinplay.item;

import java.io.Serializable;

public class UserGameItem implements Serializable {
    private static final long serialVersionUID = 4578629219631870647L;

    private String loginname;
    private int bc1s;
    private int bc1m;
    private int xd1s;
    private int xd1m;
    private int tx1s;
    private int tx1m;

    private int xs1s;
    private int xs45s;
    private int xs1m;
    private int xs1m30s;
    private int xs2m;
    private int xs5m;

    private int binhdinh;
    private int gialai;
    private int thuathienhue;
    private int danang;
    private int daclac;
    private int dacnong;
    private int khanhhoa;
    private int kontum;
    private int ninhthuan;
    private int phuyen;
    private int quangbinh;
    private int quangnam;
    private int quangngai;
    private int quangtri;
    private int mienbacdb;
    private int mienbac;
    private int binhthuan;
    private int baclieu;
    private int bentre;
    private int camau;
    private int cantho;
    private int haugiang;
    private int hochiminh;
    private int soctrang;
    private int tiengiang;
    private int travinh;
    private int tayninh;
    private int vinhlong;
    private int vungtau;
    private int dalat;
    private int dongnai;
    private int dongthap;
    private int angiang;
    private int binhduong;
    private int longan;
    private int binhphuoc;
    private int kiengiang;

    private int hnvip;
    private int hcmvip;

    private int pp;
    private int pp5p;
    private int ppmb;

    private int sacasino;
    private int ebetcasino;
    private int wmcasino;
    private int sgcasino;
    private int ptcasino;
    private int agcasino;
    private int bbincasino;

    private int sacasinotransf;
    private int ebetcasinotransf;
    private int wmcasinotransf;
    private int sgcasinotransf;
    private int ptcasinotransf;
    private int agcasinotransf;
    private int bbincasinotransf;

    private int tdslot;
    private int habaslot;
    private int ptslot;
    private int bbinslot;

    private int tdslottransf;
    private int habaslottransf;
    private int ptslottransf;
    private int bbinslottransf;

    private int cmdsport;
    private int esport;
    private int ibc2sport;
    private int sbosport;

    private int cmdsporttransf;
    private int esporttransf;
    private int ibc2sporttransf;
    private int sbosporttransf;

    private int gg;
    private int ggtransf;

    private String updatetime;
    private String updateby;

    public int countMienNam(){
        return hochiminh+dongthap+camau+bentre+vungtau+baclieu+dongnai+cantho+soctrang+
                tayninh+angiang+binhthuan+vinhlong+binhduong+travinh+longan+binhphuoc+
                haugiang+tiengiang+kiengiang+dalat;
    }
    public int countMienTrung(){
        return kontum+thuathienhue+phuyen+daclac+quangnam+danang+khanhhoa+binhdinh+
                quangtri+quangbinh+gialai+ninhthuan+quangngai+dacnong;
    }
    public int countMienBac(){
        return mienbac+mienbacdb;
    }
    public int countMienVip(){
        return hnvip+hcmvip;
    }
    public int countFast(){
        return xs1s+xs45s+xs1m+xs1m30s+xs2m+xs5m;
    }
    public int countPingpong(){
        return pp+pp5p+ppmb;
    }
    public int countDangian(){
        return bc1s+bc1m+tx1s+tx1m+xd1s+xs1m
                ;
    }
    public int countCasino(){
        return ebetcasino+sacasino+agcasino+ptcasino
                +sgcasino+wmcasino+bbincasino
                ;
    }
    public int countSlots(){
        return tdslot+habaslot+ptslot+bbinslot;
    }
    public int countSport(){
        return cmdsport+esport+ibc2sport+sbosport;
    }
    public int countOther(){
        return gg;
    }

    public void offAll() {
        this.bc1s=0;
        this.bc1m=0;
        this.xd1s=0;
        this.xd1m=0;
        this.tx1s=0;
        this.tx1m=0;

        this.xs1s=0;
        this.xs45s=0;
        this.xs1m=0;
        this.xs1m30s=0;
        this.xs2m=0;
        this.xs5m=0;


        this.binhdinh=0;
        this.gialai=0;
        this.thuathienhue=0;
        this.danang=0;
        this.daclac=0;
        this.dacnong=0;
        this.khanhhoa=0;
        this.kontum=0;
        this.ninhthuan=0;
        this.phuyen=0;
        this.quangbinh=0;
        this.quangnam=0;
        this.quangngai=0;
        this.quangtri=0;
        this.mienbacdb=0;
        this.mienbac=0;
        this.binhthuan=0;
        this.baclieu=0;
        this.bentre=0;
        this.camau=0;
        this.cantho=0;
        this.haugiang=0;
        this.hochiminh=0;
        this.soctrang=0;
        this.tiengiang=0;
        this.travinh=0;
        this.tayninh=0;
        this.vinhlong=0;
        this.vungtau=0;
        this.dalat=0;
        this.dongnai=0;
        this.dongthap=0;
        this.angiang=0;
        this.binhduong=0;
        this.longan=0;
        this.binhphuoc=0;
        this.kiengiang=0;

        this.hnvip=0;
        this.hcmvip=0;

        this.pp=0;
        this.pp5p=0;
        this.ppmb=0;

        this.sacasino=0;
        this.ebetcasino=0;
        this.wmcasino=0;
        this.sgcasino=0;
        this.ptcasino=0;
        this.agcasino=0;
        this.bbincasino=0;

        this.sacasinotransf =0;
        this.ebetcasinotransf =0;
        this.wmcasinotransf =0;
        this.sgcasinotransf =0;
        this.ptcasinotransf =0;
        this.agcasinotransf =0;
        this.bbincasinotransf =0;

        this.tdslot=0;
        this.habaslot=0;
        this.ptslot=0;
        this.bbinslot=0;

        this.tdslottransf =0;
        this.habaslottransf =0;
        this.ptslottransf =0;
        this.bbinslottransf =0;

        this.cmdsport=0;
        this.esport=0;
        this.ibc2sport=0;
        this.sbosport=0;

        this.cmdsporttransf =0;
        this.esporttransf =0;
        this.ibc2sporttransf =0;
        this.sbosporttransf =0;

        this.gg=0;
        this.ggtransf=0;
    }
    public void onAll() {
        this.bc1s=1;
        this.bc1m=1;
        this.xd1s=1;
        this.xd1m=1;
        this.tx1s=1;
        this.tx1m=1;

        this.xs1s=1;
        this.xs45s=1;
        this.xs1m=1;
        this.xs1m30s=1;
        this.xs2m=1;
        this.xs5m=1;



        this.binhdinh=1;
        this.gialai=1;
        this.thuathienhue=1;
        this.danang=1;
        this.daclac=1;
        this.dacnong=1;
        this.khanhhoa=1;
        this.kontum=1;
        this.ninhthuan=1;
        this.phuyen=1;
        this.quangbinh=1;
        this.quangnam=1;
        this.quangngai=1;
        this.quangtri=1;
        this.mienbacdb=1;
        this.mienbac=1;
        this.binhthuan=1;
        this.baclieu=1;
        this.bentre=1;
        this.camau=1;
        this.cantho=1;
        this.haugiang=1;
        this.hochiminh=1;
        this.soctrang=1;
        this.tiengiang=1;
        this.travinh=1;
        this.tayninh=1;
        this.vinhlong=1;
        this.vungtau=1;
        this.dalat=1;
        this.dongnai=1;
        this.dongthap=1;
        this.angiang=1;
        this.binhduong=1;
        this.longan=1;
        this.binhphuoc=1;
        this.kiengiang=1;

        this.hnvip=1;
        this.hcmvip=1;

        this.pp=1;
        this.pp5p=1;
        this.ppmb=1;

        this.sacasino=1;
        this.ebetcasino=1;
        this.wmcasino=1;
        this.sgcasino=1;
        this.ptcasino=1;
        this.agcasino=1;
        this.bbincasino=1;

        this.sacasinotransf =1;
        this.ebetcasinotransf =1;
        this.wmcasinotransf =1;
        this.sgcasinotransf =1;
        this.ptcasinotransf =1;
        this.agcasinotransf =1;
        this.bbincasinotransf =1;

        this.tdslot=1;
        this.habaslot=1;
        this.ptslot=1;
        this.bbinslot=1;

        this.tdslottransf =1;
        this.habaslottransf =1;
        this.ptslottransf =1;
        this.bbinslottransf =1;

        this.cmdsport=1;
        this.esport=1;
        this.ibc2sport=1;
        this.sbosport=1;

        this.cmdsporttransf =1;
        this.esporttransf =1;
        this.ibc2sporttransf =1;
        this.sbosporttransf =1;

        this.gg=1;
        this.ggtransf=1;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public int getBc1s() {
        return bc1s;
    }

    public void setBc1s(int bc1s) {
        this.bc1s = bc1s;
    }

    public int getBc1m() {
        return bc1m;
    }

    public void setBc1m(int bc1m) {
        this.bc1m = bc1m;
    }

    public int getXd1s() {
        return xd1s;
    }

    public void setXd1s(int xd1s) {
        this.xd1s = xd1s;
    }

    public int getXd1m() {
        return xd1m;
    }

    public void setXd1m(int xd1m) {
        this.xd1m = xd1m;
    }

    public int getTx1s() {
        return tx1s;
    }

    public void setTx1s(int tx1s) {
        this.tx1s = tx1s;
    }

    public int getTx1m() {
        return tx1m;
    }

    public void setTx1m(int tx1m) {
        this.tx1m = tx1m;
    }

    public int getXs1s() {
        return xs1s;
    }

    public void setXs1s(int xs1s) {
        this.xs1s = xs1s;
    }

    public int getXs45s() {
        return xs45s;
    }

    public void setXs45s(int xs45s) {
        this.xs45s = xs45s;
    }

    public int getXs1m() {
        return xs1m;
    }

    public void setXs1m(int xs1m) {
        this.xs1m = xs1m;
    }

    public int getXs1m30s() {
        return xs1m30s;
    }

    public void setXs1m30s(int xs1m30s) {
        this.xs1m30s = xs1m30s;
    }

    public int getXs2m() {
        return xs2m;
    }

    public void setXs2m(int xs2m) {
        this.xs2m = xs2m;
    }

    public int getXs5m() {
        return xs5m;
    }

    public void setXs5m(int xs5m) {
        this.xs5m = xs5m;
    }

    public int getSacasino() {
        return sacasino;
    }

    public void setSacasino(int sacasino) {
        this.sacasino = sacasino;
    }

    public int getEbetcasino() {
        return ebetcasino;
    }

    public void setEbetcasino(int ebetcasino) {
        this.ebetcasino = ebetcasino;
    }

    public int getWmcasino() {
        return wmcasino;
    }

    public void setWmcasino(int wmcasino) {
        this.wmcasino = wmcasino;
    }

    public int getSgcasino() {
        return sgcasino;
    }

    public void setSgcasino(int sgcasino) {
        this.sgcasino = sgcasino;
    }

    public int getPtcasino() {
        return ptcasino;
    }

    public void setPtcasino(int ptcasino) {
        this.ptcasino = ptcasino;
    }

    public int getAgcasino() {
        return agcasino;
    }

    public void setAgcasino(int agcasino) {
        this.agcasino = agcasino;
    }

    public int getBbincasino() {
        return bbincasino;
    }

    public void setBbincasino(int bbincasino) {
        this.bbincasino = bbincasino;
    }

    public int getBinhdinh() {
        return binhdinh;
    }

    public void setBinhdinh(int binhdinh) {
        this.binhdinh = binhdinh;
    }

    public int getGialai() {
        return gialai;
    }

    public void setGialai(int gialai) {
        this.gialai = gialai;
    }

    public int getThuathienhue() {
        return thuathienhue;
    }

    public void setThuathienhue(int thuathienhue) {
        this.thuathienhue = thuathienhue;
    }

    public int getDanang() {
        return danang;
    }

    public void setDanang(int danang) {
        this.danang = danang;
    }

    public int getDaclac() {
        return daclac;
    }

    public void setDaclac(int daclac) {
        this.daclac = daclac;
    }

    public int getDacnong() {
        return dacnong;
    }

    public void setDacnong(int dacnong) {
        this.dacnong = dacnong;
    }

    public int getKhanhhoa() {
        return khanhhoa;
    }

    public void setKhanhhoa(int khanhhoa) {
        this.khanhhoa = khanhhoa;
    }

    public int getKontum() {
        return kontum;
    }

    public void setKontum(int kontum) {
        this.kontum = kontum;
    }

    public int getNinhthuan() {
        return ninhthuan;
    }

    public void setNinhthuan(int ninhthuan) {
        this.ninhthuan = ninhthuan;
    }

    public int getPhuyen() {
        return phuyen;
    }

    public void setPhuyen(int phuyen) {
        this.phuyen = phuyen;
    }

    public int getQuangbinh() {
        return quangbinh;
    }

    public void setQuangbinh(int quangbinh) {
        this.quangbinh = quangbinh;
    }

    public int getQuangnam() {
        return quangnam;
    }

    public void setQuangnam(int quangnam) {
        this.quangnam = quangnam;
    }

    public int getQuangngai() {
        return quangngai;
    }

    public void setQuangngai(int quangngai) {
        this.quangngai = quangngai;
    }

    public int getQuangtri() {
        return quangtri;
    }

    public void setQuangtri(int quangtri) {
        this.quangtri = quangtri;
    }

    public int getMienbacdb() {
        return mienbacdb;
    }

    public void setMienbacdb(int mienbacdb) {
        this.mienbacdb = mienbacdb;
    }

    public int getMienbac() {
        return mienbac;
    }

    public void setMienbac(int mienbac) {
        this.mienbac = mienbac;
    }

    public int getBinhthuan() {
        return binhthuan;
    }

    public void setBinhthuan(int binhthuan) {
        this.binhthuan = binhthuan;
    }

    public int getBaclieu() {
        return baclieu;
    }

    public void setBaclieu(int baclieu) {
        this.baclieu = baclieu;
    }

    public int getBentre() {
        return bentre;
    }

    public void setBentre(int bentre) {
        this.bentre = bentre;
    }

    public int getCamau() {
        return camau;
    }

    public void setCamau(int camau) {
        this.camau = camau;
    }

    public int getCantho() {
        return cantho;
    }

    public void setCantho(int cantho) {
        this.cantho = cantho;
    }

    public int getHaugiang() {
        return haugiang;
    }

    public void setHaugiang(int haugiang) {
        this.haugiang = haugiang;
    }

    public int getHochiminh() {
        return hochiminh;
    }

    public void setHochiminh(int hochiminh) {
        this.hochiminh = hochiminh;
    }

    public int getSoctrang() {
        return soctrang;
    }

    public void setSoctrang(int soctrang) {
        this.soctrang = soctrang;
    }

    public int getTiengiang() {
        return tiengiang;
    }

    public void setTiengiang(int tiengiang) {
        this.tiengiang = tiengiang;
    }

    public int getTravinh() {
        return travinh;
    }

    public void setTravinh(int travinh) {
        this.travinh = travinh;
    }

    public int getTayninh() {
        return tayninh;
    }

    public void setTayninh(int tayninh) {
        this.tayninh = tayninh;
    }

    public int getVinhlong() {
        return vinhlong;
    }

    public void setVinhlong(int vinhlong) {
        this.vinhlong = vinhlong;
    }

    public int getVungtau() {
        return vungtau;
    }

    public void setVungtau(int vungtau) {
        this.vungtau = vungtau;
    }

    public int getDalat() {
        return dalat;
    }

    public void setDalat(int dalat) {
        this.dalat = dalat;
    }

    public int getDongnai() {
        return dongnai;
    }

    public void setDongnai(int dongnai) {
        this.dongnai = dongnai;
    }

    public int getDongthap() {
        return dongthap;
    }

    public void setDongthap(int dongthap) {
        this.dongthap = dongthap;
    }

    public int getAngiang() {
        return angiang;
    }

    public void setAngiang(int angiang) {
        this.angiang = angiang;
    }

    public int getBinhduong() {
        return binhduong;
    }

    public void setBinhduong(int binhduong) {
        this.binhduong = binhduong;
    }

    public int getLongan() {
        return longan;
    }

    public void setLongan(int longan) {
        this.longan = longan;
    }

    public int getBinhphuoc() {
        return binhphuoc;
    }

    public void setBinhphuoc(int binhphuoc) {
        this.binhphuoc = binhphuoc;
    }

    public int getKiengiang() {
        return kiengiang;
    }

    public void setKiengiang(int kiengiang) {
        this.kiengiang = kiengiang;
    }

    public int getHnvip() {
        return hnvip;
    }

    public void setHnvip(int hnvip) {
        this.hnvip = hnvip;
    }

    public int getHcmvip() {
        return hcmvip;
    }

    public void setHcmvip(int hcmvip) {
        this.hcmvip = hcmvip;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getPp5p() {
        return pp5p;
    }

    public void setPp5p(int pp5p) {
        this.pp5p = pp5p;
    }

    public int getPpmb() {
        return ppmb;
    }

    public void setPpmb(int ppmb) {
        this.ppmb = ppmb;
    }

    public int getTdslot() {
        return tdslot;
    }

    public void setTdslot(int tdslot) {
        this.tdslot = tdslot;
    }

    public int getHabaslot() {
        return habaslot;
    }

    public void setHabaslot(int habaslot) {
        this.habaslot = habaslot;
    }

    public int getPtslot() {
        return ptslot;
    }

    public void setPtslot(int ptslot) {
        this.ptslot = ptslot;
    }

    public int getBbinslot() {
        return bbinslot;
    }

    public void setBbinslot(int bbinslot) {
        this.bbinslot = bbinslot;
    }

    public int getCmdsport() {
        return cmdsport;
    }

    public void setCmdsport(int cmdsport) {
        this.cmdsport = cmdsport;
    }

    public int getEsport() {
        return esport;
    }

    public void setEsport(int esport) {
        this.esport = esport;
    }

    public int getIbc2sport() {
        return ibc2sport;
    }

    public void setIbc2sport(int ibc2sport) {
        this.ibc2sport = ibc2sport;
    }

    public int getSbosport() {
        return sbosport;
    }

    public void setSbosport(int sbosport) {
        this.sbosport = sbosport;
    }

    public int getGg() {
        return gg;
    }

    public void setGg(int gg) {
        this.gg = gg;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public int getSacasinotransf() {
        return sacasinotransf;
    }

    public void setSacasinotransf(int sacasinotransf) {
        this.sacasinotransf = sacasinotransf;
    }

    public int getEbetcasinotransf() {
        return ebetcasinotransf;
    }

    public void setEbetcasinotransf(int ebetcasinotransf) {
        this.ebetcasinotransf = ebetcasinotransf;
    }

    public int getWmcasinotransf() {
        return wmcasinotransf;
    }

    public void setWmcasinotransf(int wmcasinotransf) {
        this.wmcasinotransf = wmcasinotransf;
    }

    public int getSgcasinotransf() {
        return sgcasinotransf;
    }

    public void setSgcasinotransf(int sgcasinotransf) {
        this.sgcasinotransf = sgcasinotransf;
    }

    public int getPtcasinotransf() {
        return ptcasinotransf;
    }

    public void setPtcasinotransf(int ptcasinotransf) {
        this.ptcasinotransf = ptcasinotransf;
    }

    public int getAgcasinotransf() {
        return agcasinotransf;
    }

    public void setAgcasinotransf(int agcasinotransf) {
        this.agcasinotransf = agcasinotransf;
    }

    public int getBbincasinotransf() {
        return bbincasinotransf;
    }

    public void setBbincasinotransf(int bbincasinotransf) {
        this.bbincasinotransf = bbincasinotransf;
    }

    public int getTdslottransf() {
        return tdslottransf;
    }

    public void setTdslottransf(int tdslottransf) {
        this.tdslottransf = tdslottransf;
    }

    public int getHabaslottransf() {
        return habaslottransf;
    }

    public void setHabaslottransf(int habaslottransf) {
        this.habaslottransf = habaslottransf;
    }

    public int getPtslottransf() {
        return ptslottransf;
    }

    public void setPtslottransf(int ptslottransf) {
        this.ptslottransf = ptslottransf;
    }

    public int getBbinslottransf() {
        return bbinslottransf;
    }

    public void setBbinslottransf(int bbinslottransf) {
        this.bbinslottransf = bbinslottransf;
    }

    public int getCmdsporttransf() {
        return cmdsporttransf;
    }

    public void setCmdsporttransf(int cmdsporttransf) {
        this.cmdsporttransf = cmdsporttransf;
    }

    public int getEsporttransf() {
        return esporttransf;
    }

    public void setEsporttransf(int esporttransf) {
        this.esporttransf = esporttransf;
    }

    public int getIbc2sporttransf() {
        return ibc2sporttransf;
    }

    public void setIbc2sporttransf(int ibc2sporttransf) {
        this.ibc2sporttransf = ibc2sporttransf;
    }

    public int getSbosporttransf() {
        return sbosporttransf;
    }

    public void setSbosporttransf(int sbosporttransf) {
        this.sbosporttransf = sbosporttransf;
    }

    public int getGgtransf() {
        return ggtransf;
    }

    public void setGgtransf(int ggtransf) {
        this.ggtransf = ggtransf;
    }
}
