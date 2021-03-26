package com.coding.bookscan

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Insert
import com.coding.bookscan.entity.AppDatabase
import com.coding.bookscan.entity.dao.BookDao
import com.coding.bookscan.entity.data.Book
import com.coding.bookscan.viewmodel.ScannerViewModel
import com.coding.bookscan.viewmodel.ScannerViewModelState
import com.nhaarman.mockitokotlin2.*
import okhttp3.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robin.movie.testObserver
import java.io.IOException
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
        val actualDate = Date().toString()
        val book = Book(
            0,
            "9782253169789",
            "Dôme",
            "Stephen King",
            "Le Livre de Poche",
            "Un matin d’automne, la petite ville de Chester Mill, dans le Maine, est inexplicablement et brutalement isolée du reste du monde par un champ de force invisible. Personne ne comprend ce qu’est ce dôme transparent, d’où il vient et quand – ou si – il partira. L’armée semble impuissante à ouvrir un passage tandis que les ressources à l’intérieur de Chester Mill se raréfient. Jim Rennie, premier adjoint de Chester Mill, voit tout de suite le bénéfice qu’il peut tirer de la situation, lui qui a toujours rêvé de mettre la ville sous sa coupe. Un nouvel ordre social régi par la terreur s’installe et la résistance s’organise autour de Dale Barbara, vétéran d’Irak et chef cuistot fraîchement débarqué en ville…Allégorie du totalitarisme, réflexion sur la nature humaine et sa résistance aux situations extrêmes, et page-turner irrésistible, Dôme signe le retour du King à son meilleur.Clémentine Goldszal, Elle.",
            "Fantastique / Terreur / Epouvante",
            "2013-03-06",
            "dome.png",
            actualDate
        )
        val db = mock<AppDatabase>()
        val bookDao = mock<BookDao>()
        //val insert = mock<Insert>()
        //whenever(bookDao.insertBook(book)).doReturn(Unit)
        //doNothing().when(bookDao)
        whenever(db.bookDao()).thenReturn(bookDao)
        val client = mockHttpClient("""
            {
         "title": "Dôme",
         "author": "Stephen King",
         "edition": "Le Livre de Poche",
         "release_date": "2013-03-06",
         "genre": "Fantastique / Terreur / Epouvante",
         "isbn": "9782253169789",
         "summary": "Un matin d’automne, la petite ville de Chester Mill, dans le Maine, est inexplicablement et brutalement isolée du reste du monde par un champ de force invisible. Personne ne comprend ce qu’est ce dôme transparent, d’où il vient et quand – ou si – il partira. L’armée semble impuissante à ouvrir un passage tandis que les ressources à l’intérieur de Chester Mill se raréfient. Jim Rennie, premier adjoint de Chester Mill, voit tout de suite le bénéfice qu’il peut tirer de la situation, lui qui a toujours rêvé de mettre la ville sous sa coupe. Un nouvel ordre social régi par la terreur s’installe et la résistance s’organise autour de Dale Barbara, vétéran d’Irak et chef cuistot fraîchement débarqué en ville…Allégorie du totalitarisme, réflexion sur la nature humaine et sa résistance aux situations extrêmes, et page-turner irrésistible, Dôme signe le retour du King à son meilleur.Clémentine Goldszal, Elle.",
         "image": "dome.png"
      }
        """.trimIndent())

        model.getBook("9782253169789", db, client, true, actualDate)
        Assert.assertEquals(
            listOf(
                ScannerViewModelState.Success(book, "Livre trouvé ${book.isbn}")
            ),
            observer.observedValues
        )
    }
}

private fun mockHttpClient(serializedBody: String): OkHttpClient {
    val okHttpClient: OkHttpClient = mock<OkHttpClient>()
    val remoteCall: Call = mock<Call>()
    val response: Response = Response.Builder()
        .request(Request.Builder().url("http://url.com").build())
        .protocol(Protocol.HTTP_1_1)
        .code(200).message("").body(
            ResponseBody.create(
                MediaType.parse("application/json"),
                serializedBody
            )
        )
        .build()
    `when`(remoteCall.execute()).thenReturn(response)
    //`when`(remoteCall.enqueue()).thenReturn(response)
    `when`(okHttpClient.newCall(any())).thenReturn(remoteCall)
    return okHttpClient
}