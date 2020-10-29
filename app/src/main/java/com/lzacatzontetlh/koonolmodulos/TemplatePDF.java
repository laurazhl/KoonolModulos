package com.lzacatzontetlh.koonolmodulos;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import androidx.annotation.RequiresApi;
//import androidx.core.content.FileProvider;

// Autor: Laura Zacatzontetl Hernandez
public class TemplatePDF {
    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
    private Font fSTitle = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.RED);


    private ArrayList<ByteArrayOutputStream> streamList;

    public TemplatePDF(Context context) {
        this.context = context;
    }
    public void openDocument() {
        createFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        } catch (Exception e) { Log.e("openDocument", e.toString()); }
    }


    private void createFile() {
       File folder= new File(Environment.getExternalStorageDirectory() + "/documents/");
        if (!folder.exists())
            folder.mkdir();
       //pdfFile = new File(folder, "ReporteDeVentasL.pdf");
        pdfFile= new File(folder, "ReporteDeVentasL_"+ Calendar.getInstance().getTimeInMillis() + ".pdf");
       // System.out.println(" Archivo guardado:  " + pdfFile.getAbsolutePath());
       //Toast.makeText(context, "Archivo alma " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        Toast.makeText(context, "Archivo almacenado en: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
    }
    public void closeDocument() {
        document.close();
    }

    public void addMetaData(String title, String subject, String author) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle, String date) {
        try {
            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fTitle));
            addChildP(new Paragraph(subTitle, fSubTitle));

            addChildP(new Paragraph("Generado: " + date, fHighText));
            paragraph.setSpacingAfter(30);
            document.add(paragraph);
        }
        catch (Exception e) {
            Log.e("addTitles", e.toString()); }
    }

    public void sTitles(String title) {
        try {
            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fSTitle));
            document.add(paragraph);
        } catch (Exception e) { Log.e("sTitles", e.toString()); }
    }

    private void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    private void addChildST(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(childParagraph);
    }

    public void addParagraph(String text) {
        try {
            paragraph = new Paragraph(text, fText);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        } catch (Exception e) { Log.e("addParagraph", e.toString()); }
    }

    public void addImagenes(String[] header, Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());
            imagen.scaleToFit(150, 150);
            PdfPTable tabla = new PdfPTable(header.length);
            tabla.setSpacingBefore(20);//es para dejar un espacio entre las filas
            tabla.setWidthPercentage(40);
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(imagen);
            document.add(tabla);
        } catch (Exception e) {
            Log.e("Fallo metodo imagenes", e.toString());
        }
    }


    public void addImagenFirma(String[] header, Bitmap bitmap, String nombreAutor) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());
            imagen.scaleToFit(50, 50);
            imagen.setAlignment(Element.ALIGN_CENTER);
            PdfPTable tabla = new PdfPTable(header.length);
            PdfPCell celda0 = new PdfPCell(new Paragraph("ATENTAMENTE"));
            PdfPCell celda1 = new PdfPCell(new Paragraph(nombreAutor));
            PdfPCell celda2 = new PdfPCell(new Paragraph("______________________________"));
            celda0.setBorder(Rectangle.NO_BORDER);//elimina el borde de la celda
            celda1.setBorder(Rectangle.NO_BORDER);
            celda2.setBorder(Rectangle.NO_BORDER);
            celda0.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda2.setHorizontalAlignment(Element.ALIGN_CENTER);
            // celda.setFixedHeight(50);
            tabla.getDefaultCell().setBorder(0);//elimina el borde de tabhla
            tabla.setWidthPercentage(40);
            tabla.addCell(celda0);
            tabla.addCell(imagen);
            tabla.addCell(celda2);
            tabla.addCell(celda1);

            document.add(tabla);
        } catch (Exception e) {
            Log.e("Fall meto imag firma", e.toString());
        }
    }

    public void createTable(String[] header, List<String[]> clients) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable tabla = new PdfPTable(header.length);
            //tabla.setSpacingBefore(20);//es para dejar un espacio entre las filas
            tabla.setWidthPercentage(100);
            PdfPCell celda;
            int indiceColumna = 0;

            while (indiceColumna < header.length) {/// recorre lo que tenca el encabezado establecido en el main
                celda = new PdfPCell(new Phrase(header[indiceColumna++], fSubTitle));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                //celda.setBackgroundColor(new BaseColor(76, 162, 243));
                celda.setBackgroundColor(new BaseColor(0, 188, 212));

                tabla.addCell(celda);
            }

            int indiceFila;
            for (indiceFila = 0; indiceFila < clients.size(); indiceFila++) {
                String[] row = clients.get(indiceFila);
                for (indiceColumna = 0; indiceColumna < header.length; indiceColumna++) {
                    celda = new PdfPCell(new Phrase(row[indiceColumna]));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setFixedHeight(24);
                    //celda.setBorder(Rectangle.NO_BORDER); //quita las lineas de la celda
                    tabla.addCell(celda);
                }
            }

            paragraph.add(tabla);
            document.add(paragraph);
            // document.add(tabla);
        } catch (Exception e) {
            Log.e("craeteTable", e.toString());
            // System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222222222-------------------111craeteTable" + e.toString());
        }
    }

   /* public void createTable(String[] header, ArrayList<String[]> clients) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(fText);
            PdfPTable tabla = new PdfPTable(header.length);
            //tabla.setSpacingBefore(20);//es para dejar un espacio entre las filas
            tabla.setWidthPercentage(100);
            PdfPCell celda;
            int indiceColumna = 0;

            while (indiceColumna < header.length) {/// recorre lo que tenca el encabezado establecido en el main
                celda = new PdfPCell(new Phrase(header[indiceColumna++], fSubTitle));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setBackgroundColor(new BaseColor(76, 162, 243));
                tabla.addCell(celda);
            }

            int indiceFila;
            for (indiceFila = 0; indiceFila < clients.size(); indiceFila++) {
                String[] row = clients.get(indiceFila);
                for (indiceColumna = 0; indiceColumna < header.length; indiceColumna++) {
                    celda = new PdfPCell(new Phrase(row[indiceColumna]));
                    celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda.setFixedHeight(40);
                    //celda.setBorder(Rectangle.NO_BORDER); //quita las lineas de la celda
                    tabla.addCell(celda);
                }
            }

            paragraph.add(tabla);
            document.add(paragraph);
            // document.add(tabla);
        } catch (Exception e) {
            Log.e("craeteTable", e.toString());
           // System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222222222-------------------111craeteTable" + e.toString());
        }
    }*/
    public void viewPDF() {
        Intent intent = new Intent(context, com.lzacatzontetlh.koonolmodulos.ViewPDFActivity.class);
        intent.putExtra("path", pdfFile.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
   public void appViewPDF( Context context) {
       Uri pdf = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", pdfFile);
       Intent pdfIntent = new Intent(Intent.ACTION_VIEW, pdf);
       pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       PackageManager packageManager =context.getApplicationContext().getPackageManager();
       List<ResolveInfo> activities = packageManager.queryIntentActivities(pdfIntent, 0);
       boolean isIntentSafe = activities.size() > 0;
       if (isIntentSafe) {
           context.startActivity(pdfIntent);
       }
   }

}
// Autor: Laura Zacatzontetl Hernandez