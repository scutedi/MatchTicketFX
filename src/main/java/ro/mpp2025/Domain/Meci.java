package ro.mpp2025.Domain;

import java.util.Objects;

public class Meci extends Entity <Integer>{

    private Echipa EchipaA;
    private Echipa EchipaB;
    private String nume_meci;
    private Integer nr_loc;
    private Integer pret;

    public Meci(Echipa EchipaA , Echipa EchipaB, String nume_meci, Integer nr_loc, Integer pret) {
        this.EchipaA = EchipaA;
        this.EchipaB = EchipaB;
        this.nume_meci = nume_meci;
        this.nr_loc = nr_loc;
        this.pret = pret;
    }

    public Echipa getEchipaA() {
        return EchipaA;
    }
    public void setEchipaA(Echipa EchipaA) {
        this.EchipaA = EchipaA;

    }
    public Echipa getEchipaB() {
        return EchipaB;
    }
    public void setEchipaB(Echipa EchipaB) {
        this.EchipaB = EchipaB;
    }
    public String getNume_meci() {
        return nume_meci;
    }
    public void setNume_meci(String nume_meci) {
        this.nume_meci = nume_meci;
    }
    public Integer getNr_loc() {
        return nr_loc;
    }
    public void setNr_loc(Integer nr_loc) {
        this.nr_loc = nr_loc;
    }
    public Integer getPret() {
        return pret;
    }
    public void setPret(Integer pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Meci {" +
                "idEchipaA='" + EchipaA + '\'' +
                "idEchipaB='" + EchipaB + '\'' +
                "nume_meci='" + nume_meci + '\'' +
                "nr_loc='" + nr_loc + '\'' +
                "pret='" + pret + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Meci that = (Meci) o;
        return Objects.equals(EchipaA, that.EchipaA) && Objects.equals(EchipaB , that.EchipaB) && Objects.equals(nume_meci , that.nume_meci) && Objects.equals(nr_loc , that.nr_loc) && Objects.equals(pret , that.pret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), EchipaA , EchipaB , nume_meci , nr_loc , pret);
    }
}
