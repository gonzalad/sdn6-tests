package com.example.sdn6.entity;

public enum TypeChampAdditionnel {

    VALEURBOOLEENNE, VALEURNUM, VALEURSTRING;

    @SuppressWarnings("unchecked")
    public <T> ValeurChampAdditionnelDomain<T> castValeur(Object o) {
        if (o == null)
            return null;
        switch (this) {
        case VALEURBOOLEENNE:
            return (ValeurChampAdditionnelDomain<T>)new ValeurChampAdditionnelDomain<Boolean>() {
                @Override
                public TypeChampAdditionnel getType() {
                    return VALEURBOOLEENNE;
                }

                @Override
                public Boolean getValeur() {
                    return (Boolean)o;
                }
            };
        case VALEURNUM:
            return (ValeurChampAdditionnelDomain<T>)new ValeurChampAdditionnelDomain<Double>() {
                @Override
                public TypeChampAdditionnel getType() {
                    return VALEURNUM;
                }

                @Override
                public Double getValeur() {
                    return ((Number)o).doubleValue();
                }
            };
        case VALEURSTRING:
            return (ValeurChampAdditionnelDomain<T>)new ValeurChampAdditionnelDomain<String>() {
                @Override
                public TypeChampAdditionnel getType() {
                    return VALEURSTRING;
                }

                @Override
                public String getValeur() {
                    return (String)o;
                }
            };
        default:
            throw new ClassCastException();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T cast(Object o) {
        if (o == null)
            return null;
        switch (this) {
        case VALEURBOOLEENNE:
            return (T)((Boolean)o);
        case VALEURNUM:
            return (T)(Double)o;
        case VALEURSTRING:
            return (T)(String)o;
        default:
            throw new ClassCastException();
        }
    }
}
