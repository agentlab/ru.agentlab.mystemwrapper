package ru.agentlab.mystemwrapper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Evgeny on 22.12.2014.
 */
public class Word {

    public static class Lexema {
        private String value;
        private double weight;
        private Set<GrammemeType> grammemes = new HashSet<GrammemeType>();

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public Set<GrammemeType> getGrammemes() {
            return grammemes;
        }

        public void setGrammemes(Set<GrammemeType> grammemes) {
            this.grammemes = grammemes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lexema)) return false;

            Lexema lexema = (Lexema) o;

            if (Double.compare(lexema.weight, weight) != 0) return false;
            if (grammemes != null ? !grammemes.equals(lexema.grammemes) : lexema.grammemes != null) return false;
            if (value != null ? !value.equals(lexema.value) : lexema.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = value != null ? value.hashCode() : 0;
            temp = Double.doubleToLongBits(weight);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (grammemes != null ? grammemes.hashCode() : 0);
            return result;
        }

        @Override
        public String toString()
        {
            return "Lexema{" +
                    "value='" + value + '\'' +
                    ", weight=" + weight +
                    ", grammemes=" + grammemes +
                    '}';
        }
    }

    private String value;
    private Set<Lexema> lexemas = new HashSet<Lexema>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Lexema> getLexemas() {
        return lexemas;
    }

    public void setLexemas(Set<Lexema> lexemas) {
        this.lexemas = lexemas;
    }

    @Override
    public String toString()
    {
        return "Word{" +
                "value='" + value + '\'' +
                ", lexemas=" + lexemas +
                '}';
    }
}
