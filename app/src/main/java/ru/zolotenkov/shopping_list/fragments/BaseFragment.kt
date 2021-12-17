package ru.zolotenkov.shopping_list.fragments

import androidx.fragment.app.Fragment

//В нём будет мелкий функционал, по этому делаем абстрактным (мы не можем создать объект подобного класса, только наследовать)
abstract class BaseFragment: Fragment() {
    abstract fun onClickNew()

}