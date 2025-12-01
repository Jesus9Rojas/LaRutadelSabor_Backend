// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/dto/PedidoHistorialDTO.java

package com.RutaDelSabor.ruta.dto;

import com.RutaDelSabor.ruta.models.entities.Pedido;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoHistorialDTO {
    private Long id;
    private Date fechaPedido;
    private BigDecimal subtotal;
    private BigDecimal montoAgregado;
    private BigDecimal total;
    private String estadoActual;
    private List<ItemHistorialDTO> items;

    // Constructores, Getters y Setters...
    public PedidoHistorialDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Date fechaPedido) { this.fechaPedido = fechaPedido; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getMontoAgregado() { return montoAgregado; }
    public void setMontoAgregado(BigDecimal montoAgregado) { this.montoAgregado = montoAgregado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public List<ItemHistorialDTO> getItems() { return items; }
    public void setItems(List<ItemHistorialDTO> items) { this.items = items; }

    public static PedidoHistorialDTO fromEntity(Pedido pedido) {
        if (pedido == null) return null;

        String estado = pedido.getEstadoActual();

        List<ItemHistorialDTO> itemsDto = pedido.getDetalles() == null ? null :
                pedido.getDetalles().stream()
                        .map(ItemHistorialDTO::fromEntity)
                        .collect(Collectors.toList());

        PedidoHistorialDTO dto = new PedidoHistorialDTO();
        dto.setId(pedido.getId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setMontoAgregado(pedido.getMontoAgregado());
        dto.setTotal(pedido.getTotal());
        dto.setEstadoActual(estado);
        dto.setItems(itemsDto);
        
        return dto;
    }

    // Clase interna para los items del historial
    public static class ItemHistorialDTO {
        private String nombreProducto;
        private int cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotalItem;

        public ItemHistorialDTO(String nombreProducto, int cantidad, BigDecimal precioUnitario, BigDecimal subtotalItem) {
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotalItem = subtotalItem;
        }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public BigDecimal getSubtotalItem() { return subtotalItem; }
        public void setSubtotalItem(BigDecimal subtotalItem) { this.subtotalItem = subtotalItem; }

        public static ItemHistorialDTO fromEntity(com.RutaDelSabor.ruta.models.entities.PedidoDetallado detalle) {
            if (detalle == null || detalle.getProducto() == null) return null;
            return new ItemHistorialDTO(
                    detalle.getProducto().getProducto(),
                    detalle.getCantidad(),
                    detalle.getProducto().getPrecio(),
                    detalle.getSubtotal()
            );
        }
    }
}