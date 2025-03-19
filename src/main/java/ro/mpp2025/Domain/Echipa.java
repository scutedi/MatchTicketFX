package ro.mpp2025.Domain;

import java.util.Objects;

public class Echipa extends Entity <Integer> {
    private String name;

    public Echipa(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Echipa{" +
                "Nume='" + name + ',' +
                "Id='" + getId() + '\'' + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Echipa that = (Echipa) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
