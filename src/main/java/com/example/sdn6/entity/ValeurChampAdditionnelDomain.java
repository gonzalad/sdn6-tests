package com.example.sdn6.entity;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import static com.example.sdn6.entity.TypeChampAdditionnel.*;

/**
 * Valeur d'un champs additionnel de l'objet ObjetFormationDomain
 * additionnel permet de conserver une seule classe (ObjetFormationDomain) pour
 * représenter plusieurs type de noeuds différents.
 */
public interface ValeurChampAdditionnelDomain<T> {

    TypeChampAdditionnel getType();

    T getValeur();

    default Boolean getValeurBooleene() {
        TypeChampAdditionnel type = getType();
        if (type != null && !VALEURBOOLEENNE.equals(type))
            throw new ClassCastException();
        return (Boolean)getValeur();
    }

    default Double getValeurNum() {
        TypeChampAdditionnel type = getType();
        if (type != null && !VALEURNUM.equals(type))
            throw new ClassCastException();
        return (Double)getValeur();
    }

    default String getValeurString() {
        TypeChampAdditionnel type = getType();
        if (type != null && !VALEURSTRING.equals(type))
            throw new ClassCastException();
        return (String)getValeur();
    }

    /**
     * @return generic implementation
     */
    static <T> ValeurChampAdditionnelDomain<T> valeurChampAdditionnel(TypeChampAdditionnel type, T valeur) {
        return valeurChampAdditionnel(() -> type, valeur);
    }

    /**
     * @return generic lazy implementation
     */
    static <T> ValeurChampAdditionnelDomain<T> valeurChampAdditionnel(Supplier<TypeChampAdditionnel> type, T valeur) {
        return new ValeurChampAdditionnelDomain<T>() {
            @Override
            public TypeChampAdditionnel getType() {
                return type.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public T getValeur() {
                return Optional.ofNullable(type.get()).map(type -> (T)type.cast(valeur)).orElse(valeur);
            }
        };
    }

    static boolean isSame(ValeurChampAdditionnelDomain<?> o1, ValeurChampAdditionnelDomain<?> o2) {
        if (o1 == null || o2 == null) {
            return o1 == null && o2 == null;
        }
        boolean equals = Objects.equals(o1.getValeur(), o2.getValeur());
        equals &= Objects.equals(o1.getType(), o2.getType());
        return equals;
    }
}
