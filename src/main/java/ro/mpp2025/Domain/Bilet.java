package ro.mpp2025.Domain;

import java.util.Objects;

public class Bilet extends Entity <Integer>{

    private Meci meci_id;
    private Client client_id;

    public Bilet(Meci meci_id, Client client_id) {
        this.meci_id = meci_id;
        this.client_id = client_id;
    }
    public Meci getMeci_id() {
        return meci_id;
    }
    public void setMeci_id(Meci meci_id) {
        this.meci_id = meci_id;
    }
    public Client getClient_id() {
        return client_id;
    }
    public void setClient_id(Client client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString() {
        return "Bilete{" +
                "meci_id='" + meci_id + '\'' +
                ", client_id='" + client_id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bilet that = (Bilet) o;
        return Objects.equals(meci_id, that.meci_id) && Objects.equals(client_id, that.client_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), meci_id, client_id);
    }
}
