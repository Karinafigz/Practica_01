 package com.example.practica_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica_01.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_view_design.*

 class MainActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMainBinding
     val data = ArrayList<Alumno>()
     val adapter = AlumnoAdapter(this, data)
     var idAlumno: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //abrimos conexión
        val dbconx =DBHelperAlumno(this)

        //abrimos la base
        val db= dbconx.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM alumnos", null)

        if(cursor.moveToFirst()){
            do{

            idAlumno = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                var itemNom=cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                var itemCue=cursor.getString(cursor.getColumnIndexOrThrow("cuenta"))
                var itemCorr=cursor.getString(cursor.getColumnIndexOrThrow("correo"))
                var itemImg=cursor.getString(cursor.getColumnIndexOrThrow("imagen"))

                data.add(
                    Alumno("$itemNom",
                        "$itemCue",
                        "$itemCorr",
                        "$itemImg"
                    )
                )
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        dbconx.close()
        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)


       /*
        //Agregar elementos a la lista
       data.add(
            Alumno(
                "Karina Figueroa",
                "20170073",
                "kfigueroa@ucol.mx",
                "http://3.bp.blogspot.com/-jYzD2sf49c8/VNYUqIm-_JI/AAAAAAAAOPM/tiQLrK6xt-c/s1600/dibujo_animado_yorkshire_terrier_perrito_cortado_escultura_fotogr%25C3%25A1fica-r4c278034ec51472ea946399d2dea9c9f_x7sai_8byvr_512.jpg"
            )
        )
        data.add(
            Alumno(
                "George Rubio",
                "20170079",
                "grubio@ucol.mx",
                "https://i.pinimg.com/236x/66/50/6a/66506ad46054f55f7f3a48657668ab89--fox-terriers-koko.jpg"
            )
        )
        data.add(
            Alumno(
                "Sebastian Chocoteco",
                "20170934",
                "schocoteco@ucol.mx",
                "https://i.pinimg.com/originals/25/20/00/25200034ef535eb2d4ab1de0aa352d57.png"
            )
        )
        data.add(
            Alumno(
                "René Valencia",
                "20177013",
                "rvalencia7@ucol.mx",
                "https://i.pinimg.com/474x/9d/0a/06/9d0a06a068dc3c6fd2dc103a68c60493--cute-fluffy-puppies-bichon-frise.jpg"
            )
        )
        data.add(
            Alumno(
                "Cesar Araujo",
                "20170979",
                "caraujo@ucol.mx",
                "https://i.pinimg.com/564x/a2/76/43/a27643fcd19fe681a8bbf16fce407718.jpg"
            )
        )
        data.add(
            Alumno(
                "Misael Cardenas",
                "20170247",
                "mcardenas@ucol.mx",
                "https://dbdzm869oupei.cloudfront.net/img/sticker/preview/34089.png"
            )
        )

*/
        // This will pass the ArrayList to our Adapter

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object : AlumnoAdapter.ClickListener{
            override fun onItemClick(view: View, position:Int){
               // Toast.makeText(this@MainActivity,"Click en el Item ${position}",Toast.LENGTH_LONG).show()
                 itemOptionsMenu(position)

            }
        })

        //variable para recibir todos los extras
        val parExtra = intent.extras
        //variables que recibimos todos los extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val cuenta = parExtra?.getString("cuenta")
        val correo = parExtra?.getString("correo")
        val imagen = parExtra?.getString("imagen")
        //Toast.makeText(this,"${nombre}",Toast.LENGTH_LONG).show()


        ///agregarrrrrrrr

        if(msje=="nuevo"){
            val insertIndex:Int=data.count()
            data.add(insertIndex,
                Alumno(
                    "${nombre}",
                    "${cuenta}",
                    "${correo}",
                    "${imagen}"
                )
            )
            adapter.notifyItemInserted(insertIndex)
        }

        //Click en agregar
        faButton.setOnClickListener{
            val intento2= Intent(this,MainActivityNuevo::class.java)
            //intento2.putExtra("valor1","Hola mundo")
            startActivity(intento2)
        }

    }

     private fun itemOptionsMenu(position: Int) {
         val popupMenu = PopupMenu(this,binding.recyclerview[position].findViewById(R.id.textViewOptions))
         popupMenu.inflate(R.menu.options_menu)
//Para cambiarnos de activity
         val intento2 = Intent(this,MainActivityNuevo::class.java)
//Implementar el click en el item
         popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
             override fun onMenuItemClick(item: MenuItem?): Boolean {
                 when(item?.itemId){
                     R.id.borrar -> {
                         val tmpAlum = data[position]
                         data.remove(tmpAlum)
                         adapter.notifyDataSetChanged()
                         return true
                     }
                     R.id.editar ->{
                         //Tomamos los datos del alumno, en la posición de la lista donde hicieron click
                         val nombre = data[position].nombre
                         val cuenta = data[position].cuenta
                         val correo = data[position].correo
                         val image = data[position].image
                         //En position tengo el indice del elemento en la lista
                         val idAlum: Int = position
                         intento2.putExtra("mensaje","edit")
                         intento2.putExtra("nombre","${nombre}")
                         intento2.putExtra("cuenta","${cuenta}")
                         intento2.putExtra("correo","${correo}")
                         intento2.putExtra("image","${image}")
                         //Pasamos por extras el idAlum para poder saber cual editar de la lista (ArrayList)
                         intento2.putExtra("idA",idAlum)
                         startActivity(intento2)
                         return true
                     }
                 }
                 return false
             }
         })
         popupMenu.show()
     }
 }


