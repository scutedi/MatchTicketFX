package ro.mpp2025.Domain;

import java.util.Objects;

public class Client extends Entity <Integer> {
    private String nume;
    private String adresa;

    public Client(String nume, String adresa) {
        this.nume = nume;
        this.adresa = adresa;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getAdresa() {
        return adresa;
    }
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "Client{" +
                "Nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client that = (Client) o;
        return Objects.equals(nume, that.nume) && Objects.equals(adresa, that.adresa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, adresa);
    }
}
