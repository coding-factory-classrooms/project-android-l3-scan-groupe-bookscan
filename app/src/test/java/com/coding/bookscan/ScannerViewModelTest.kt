package com.coding.bookscan

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.coding.bookscan.entity.AppDatabase
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.viewmodel.BookListViewModel
import com.coding.bookscan.viewmodel.ScannerViewModel
import com.coding.bookscan.viewmodel.ScannerViewModelState
import com.nhaarman.mockitokotlin2.mock
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.robin.movie.testObserver
import java.util.*

class ScannerViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `invalid isbn syntax yields Failure`() {
        val model = ScannerViewModel()
        val observer = model.getScannerState().testObserver()
        val db = mock<AppDatabase>()
        model.getBook("1234", db)
        Assert.assertEquals(
            listOf(
                ScannerViewModelState.Failure("Format de code barre incorrect")
            ),
            observer.observedValues
        )
    }

    @Test
    fun `get book yields Success`() {
        val model = ScannerViewModel()
        val observer = model.getScannerState().testObserver()
        val book = Book(0, "9782253169789", "Dôme", "Stephen King","Le Livre de Poche","Un matin d’automne, la petite ville de Chester Mill, dans le Maine, est inexplicablement et brutalement isolée du reste du monde par un champ de force invisible. Personne ne comprend ce qu’est ce dôme transparent, d’où il vient et quand – ou si – il partira. L’armée semble impuissante à ouvrir un passage tandis que les ressources à l’intérieur de Chester Mill se raréfient. Jim Rennie, premier adjoint de Chester Mill, voit tout de suite le bénéfice qu’il peut tirer de la situation, lui qui a toujours rêvé de mettre la ville sous sa coupe. Un nouvel ordre social régi par la terreur s’installe et la résistance s’organise autour de Dale Barbara, vétéran d’Irak et chef cuistot fraîchement débarqué en ville…Allégorie du totalitarisme, réflexion sur la nature humaine et sa résistance aux situations extrêmes, et page-turner irrésistible, Dôme signe le retour du King à son meilleur. Clémentine Goldszal, Elle.","Fantastique / Terreur / Epouvante","2013-03-06","dome.png", Date().toString())
        val db = mock<AppDatabase>()
        val client = mock<OkHttpClient>()

        model.getBook("9782253169789", db)
        Assert.assertEquals(
            listOf(
                ScannerViewModelState.Success(book, "Livre trouvé ${book.isbn}")
            ),
            observer.observedValues
        )
    }
}