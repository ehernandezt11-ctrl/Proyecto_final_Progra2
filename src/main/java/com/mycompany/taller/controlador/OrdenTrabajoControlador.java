
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.*;
import com.mycompany.taller.modelo.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/ordenes")
public class OrdenTrabajoControlador {

    @Autowired
    private OrdenTrabajoDao ordenTrabajoDao;
    @Autowired
    private VehiculoDao vehiculoDao;
    @Autowired
    private MecanicoDao mecanicoDao;
    

    @Autowired
    private DetalleOrdenRepuestoDao detalleOrdenRepuestoDao;
    @Autowired
    private RepuestoDao repuestoDao;

    
    @Autowired
    private DetalleOrdenServicioDao detalleOrdenServicioDao;
    @Autowired
    private ServicioDao servicioDao;

    
    @Autowired
    private TemplateEngine templateEngine;

    
    @GetMapping({"", "/"})
    public String listarOrdenes(Model model) {
        model.addAttribute("ordenes", ordenTrabajoDao.listar());
        model.addAttribute("orden", new OrdenTrabajo()); 
        model.addAttribute("vehiculos", vehiculoDao.listar());
        model.addAttribute("mecanicos", mecanicoDao.listar());
        return "ordenes";
    }


    @PostMapping("/guardar")
    public String guardarOrden(@ModelAttribute OrdenTrabajo orden) {
        ordenTrabajoDao.guardar(orden);
        return "redirect:/ordenes";
    }

    
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable("id") int idOrden, Model model) {
        OrdenTrabajo orden = ordenTrabajoDao.buscarPorId(idOrden);
        model.addAttribute("orden", orden);
        

        model.addAttribute("repuestosAsignados", detalleOrdenRepuestoDao.listarPorOrden(idOrden));
        model.addAttribute("repuestosDisponibles", repuestoDao.listar());

        
        model.addAttribute("serviciosAsignados", detalleOrdenServicioDao.listarPorOrden(idOrden));
        model.addAttribute("serviciosDisponibles", servicioDao.listar());
        
        return "detalle_orden";
    }


    @PostMapping("/agregar-repuesto")
    public String agregarRepuesto(@RequestParam("idOrden") int idOrden,
                                  @RequestParam("idRepuesto") int idRepuesto,
                                  @RequestParam("cantidad") int cantidad) {
        Repuesto repuesto = repuestoDao.buscarPorId(idRepuesto);
        if (repuesto != null) {
            double precioUnitario = repuesto.getPrecio();
            double subtotal = cantidad * precioUnitario;

            DetalleOrdenRepuesto detalle = new DetalleOrdenRepuesto();
            detalle.setIdOrden(idOrden);
            detalle.setIdRepuesto(idRepuesto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            detalleOrdenRepuestoDao.agregar(detalle);
        }
        return "redirect:/ordenes/detalle/" + idOrden;
    }


    @PostMapping("/agregar-servicio")
    public String agregarServicio(@RequestParam("idOrden") int idOrden,
                                  @RequestParam("idServicio") int idServicio) {
        Servicio servicio = servicioDao.buscarPorId(idServicio);
        if (servicio != null) {
            DetalleOrdenServicio detalle = new DetalleOrdenServicio();
            detalle.setIdOrden(idOrden);
            detalle.setIdServicio(idServicio);
            detalle.setCostoAplicado(servicio.getCostoManoObra());

            detalleOrdenServicioDao.agregar(detalle);
        }
        return "redirect:/ordenes/detalle/" + idOrden;
    }

   
    @GetMapping("/iniciar/{id}")
    public String iniciarOrden(@PathVariable("id") int id) {
        ordenTrabajoDao.actualizarEstado(id, "EN PROCESO");
        return "redirect:/ordenes";
    }

    @GetMapping("/finalizar/{id}")
    public String finalizarOrden(@PathVariable("id") int id) {
        ordenTrabajoDao.finalizarOrden(id);
        return "redirect:/ordenes";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarOrden(@PathVariable("id") int id) {
        ordenTrabajoDao.actualizarEstado(id, "CANCELADA");
        return "redirect:/ordenes";
    }


    @GetMapping("/exportar-pdf")
    public void exportarPdf(HttpServletResponse response) throws IOException {
        List<OrdenTrabajo> listaOrdenes = ordenTrabajoDao.listar();
        
        Context context = new Context();
        context.setVariable("ordenes", listaOrdenes);

        String htmlContent = templateEngine.process("ordenes-pdf", context);

        byte[] pdfBytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, "/");
            builder.toStream(os);
            builder.run();
            pdfBytes = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el PDF: " + e.getMessage());
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_ordenes_trabajo.pdf");
        response.setContentLength(pdfBytes.length);

        try (OutputStream out = response.getOutputStream()) {
            out.write(pdfBytes);
            out.flush();
        }
    }
}