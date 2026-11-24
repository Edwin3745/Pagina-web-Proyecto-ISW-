'use strict';

/**
 * Interfaz de la carta del restaurante.
 * Consume la API REST /api/v1/productos (mismo origen que sirve esta página).
 */
const API = '/api/v1/productos';

const CATEGORIAS = [
  { valor: 'ENTRADA', nombre: 'Entradas' },
  { valor: 'PLATO_FUERTE', nombre: 'Platos fuertes' },
  { valor: 'ACOMPANAMIENTO', nombre: 'Acompañamientos' },
  { valor: 'POSTRE', nombre: 'Postres' },
  { valor: 'BEBIDA', nombre: 'Bebidas' }
];

const estado = {
  productos: [],
  categoria: 'TODAS',
  soloDisponibles: false,
  editando: null,
  porEliminar: null,
  guardando: false
};

const $ = (id) => document.getElementById(id);
const formatoPrecio = new Intl.NumberFormat('es-CO', {
  style: 'currency', currency: 'COP', minimumFractionDigits: 0, maximumFractionDigits: 2
});

/** Crea un elemento; el texto siempre se asigna como textContent (nunca como HTML). */
function el(etiqueta, opciones = {}, hijos = []) {
  const nodo = document.createElement(etiqueta);
  const { clase, texto, atributos } = opciones;
  if (clase) nodo.className = clase;
  if (texto !== undefined) nodo.textContent = texto;
  if (atributos) Object.entries(atributos).forEach(([k, v]) => nodo.setAttribute(k, v));
  hijos.forEach((h) => nodo.appendChild(h));
  return nodo;
}

/* ---------------- API ---------------- */

async function pedir(url, opciones = {}) {
  let respuesta;
  try {
    respuesta = await fetch(url, {
      headers: { 'Content-Type': 'application/json', Accept: 'application/json' },
      ...opciones
    });
  } catch (e) {
    const error = new Error('No se pudo conectar con el servidor. Verifica que la aplicación esté en ejecución.');
    error.status = 0;
    throw error;
  }
  if (respuesta.status === 204) return null;

  let cuerpo = null;
  try { cuerpo = await respuesta.json(); } catch (e) { /* respuesta sin JSON */ }

  if (!respuesta.ok) {
    const error = new Error((cuerpo && cuerpo.mensaje) || 'Ocurrió un error inesperado (código ' + respuesta.status + ').');
    error.status = respuesta.status;
    error.campos = (cuerpo && cuerpo.campos) || null;
    throw error;
  }
  return cuerpo;
}

async function cargarProductos() {
  $('contenido').setAttribute('aria-busy', 'true');
  try {
    estado.productos = await pedir(API);
    renderizar();
  } catch (e) {
    renderizarError(e.message);
  } finally {
    $('contenido').setAttribute('aria-busy', 'false');
  }
}

/* ---------------- Renderizado ---------------- */

function productosVisibles() {
  return estado.productos.filter((p) =>
    (estado.categoria === 'TODAS' || p.categoria === estado.categoria) &&
    (!estado.soloDisponibles || p.disponible));
}

function renderizarFiltros() {
  const contenedor = $('filtro-categorias');
  contenedor.replaceChildren();
  [{ valor: 'TODAS', nombre: 'Todas' }, ...CATEGORIAS].forEach((c) => {
    const input = el('input', { atributos: { type: 'radio', name: 'categoria-filtro', value: c.valor } });
    input.checked = estado.categoria === c.valor;
    input.addEventListener('change', () => { estado.categoria = c.valor; renderizar(); });
    contenedor.appendChild(el('label', { clase: 'chip' }, [input, el('span', { texto: c.nombre })]));
  });
}

function renderizarResumen() {
  const total = estado.productos.length;
  const disponibles = estado.productos.filter((p) => p.disponible).length;
  $('resumen').textContent = total === 0
    ? 'La carta todavía no tiene productos.'
    : total + (total === 1 ? ' producto' : ' productos') + ', ' + disponibles +
      (disponibles === 1 ? ' disponible' : ' disponibles') + '.';
}

function crearItem(p) {
  const linea = el('div', { clase: 'item__linea' }, [
    el('span', { clase: 'item__nombre', texto: p.nombre }),
    el('span', { clase: 'item__guia', atributos: { 'aria-hidden': 'true' } }),
    el('span', { clase: 'item__precio', texto: formatoPrecio.format(p.precio) })
  ]);

  const pie = el('div', { clase: 'item__pie' });
  pie.appendChild(el('span', { clase: 'etiqueta', texto: 'Preparación: ' + p.tiempoPreparacionMin + ' min' }));
  if (!p.disponible) pie.appendChild(el('span', { clase: 'etiqueta etiqueta--agotado', texto: 'Agotado' }));

  const editar = el('button', { clase: 'enlace', texto: 'Editar', atributos: { type: 'button', 'aria-label': 'Editar ' + p.nombre } });
  editar.addEventListener('click', () => abrirFormulario(p));
  const eliminar = el('button', { clase: 'enlace enlace--peligro', texto: 'Eliminar', atributos: { type: 'button', 'aria-label': 'Eliminar ' + p.nombre } });
  eliminar.addEventListener('click', () => abrirEliminar(p));
  pie.appendChild(el('div', { clase: 'item__acciones' }, [editar, eliminar]));

  const hijos = [linea];
  if (p.descripcion) hijos.push(el('p', { clase: 'item__descripcion', texto: p.descripcion }));
  hijos.push(pie);
  return el('li', { clase: 'item' + (p.disponible ? '' : ' item--agotado') }, hijos);
}

function renderizar() {
  renderizarFiltros();
  renderizarResumen();
  $('solo-disponibles').checked = estado.soloDisponibles;

  const contenido = $('contenido');
  contenido.replaceChildren();

  if (estado.productos.length === 0) {
    const agregar = el('button', { clase: 'boton boton--primario', texto: 'Agregar producto', atributos: { type: 'button' } });
    agregar.addEventListener('click', () => abrirFormulario(null));
    contenido.appendChild(el('div', { clase: 'estado' }, [
      el('h2', { texto: 'Tu carta está vacía' }),
      el('p', { texto: 'Agrega el primer plato o bebida para empezar a armar el menú.' }),
      agregar
    ]));
    return;
  }

  const visibles = productosVisibles();
  if (visibles.length === 0) {
    const limpiar = el('button', { clase: 'boton', texto: 'Quitar filtros', atributos: { type: 'button' } });
    limpiar.addEventListener('click', () => { estado.categoria = 'TODAS'; estado.soloDisponibles = false; renderizar(); });
    contenido.appendChild(el('div', { clase: 'estado' }, [
      el('h2', { texto: 'Sin resultados' }),
      el('p', { texto: 'Ningún producto coincide con los filtros elegidos.' }),
      limpiar
    ]));
    return;
  }

  CATEGORIAS.forEach((cat) => {
    const deCategoria = visibles.filter((p) => p.categoria === cat.valor);
    if (deCategoria.length === 0) return;
    const lista = el('ul', { clase: 'items' }, deCategoria.map(crearItem));
    contenido.appendChild(el('section', { clase: 'categoria', atributos: { 'aria-label': cat.nombre } }, [
      el('h2', { clase: 'categoria__titulo' }, [
        el('span', { texto: cat.nombre }),
        el('span', { clase: 'categoria__conteo', texto: String(deCategoria.length) })
      ]),
      lista
    ]));
  });
}

function renderizarError(mensaje) {
  $('resumen').textContent = 'No se pudo cargar la carta.';
  const reintentar = el('button', { clase: 'boton', texto: 'Reintentar', atributos: { type: 'button' } });
  reintentar.addEventListener('click', cargarProductos);
  $('contenido').replaceChildren(el('div', { clase: 'estado estado--error', atributos: { role: 'alert' } }, [
    el('h2', { texto: 'No se pudo cargar la carta' }),
    el('p', { texto: mensaje }),
    reintentar
  ]));
}

/* ---------------- Avisos ---------------- */

let temporizadorAviso = null;
function avisar(mensaje, esError = false) {
  const aviso = $('aviso');
  aviso.textContent = mensaje;
  aviso.classList.toggle('aviso--error', esError);
  aviso.classList.add('aviso--visible');
  clearTimeout(temporizadorAviso);
  temporizadorAviso = setTimeout(() => aviso.classList.remove('aviso--visible'), 4000);
}

/* ---------------- Formulario agregar / editar ---------------- */

const CAMPOS = ['nombre', 'descripcion', 'precio', 'categoria'];

function limpiarErrores() {
  CAMPOS.forEach((c) => {
    $('err-' + c).textContent = '';
    $('f-' + c).removeAttribute('aria-invalid');
    $('f-' + c).removeAttribute('aria-describedby');
  });
  $('err-general').textContent = '';
}

function mostrarErrorCampo(campo, mensaje) {
  if (!CAMPOS.includes(campo)) return false;
  $('err-' + campo).textContent = mensaje;
  $('f-' + campo).setAttribute('aria-invalid', 'true');
  $('f-' + campo).setAttribute('aria-describedby', 'err-' + campo);
  return true;
}

function actualizarContador() {
  $('contador').textContent = $('f-descripcion').value.length + ' de 500 caracteres';
}

function abrirFormulario(producto) {
  estado.editando = producto;
  limpiarErrores();
  const select = $('f-categoria');
  if (select.options.length === 0) {
    CATEGORIAS.forEach((c) => select.appendChild(el('option', { texto: c.nombre, atributos: { value: c.valor } })));
  }

  $('dlg-titulo').textContent = producto ? 'Editar producto' : 'Agregar producto';
  $('btn-guardar').textContent = producto ? 'Guardar cambios' : 'Agregar';
  $('f-nombre').value = producto ? producto.nombre : '';
  $('f-descripcion').value = producto && producto.descripcion ? producto.descripcion : '';
  $('f-precio').value = producto ? producto.precio : '';
  select.value = producto ? producto.categoria : (estado.categoria !== 'TODAS' ? estado.categoria : CATEGORIAS[0].valor);
  select.disabled = Boolean(producto);
  $('ayuda-categoria').hidden = !producto;
  $('f-disponible').checked = producto ? producto.disponible : true;
  actualizarContador();

  $('dlg-producto').showModal();
  $('f-nombre').focus();
}

function validarLocal(datos) {
  let valido = true;
  if (!datos.nombre) { mostrarErrorCampo('nombre', 'Escribe el nombre del producto.'); valido = false; }
  if (!(datos.precio > 0)) { mostrarErrorCampo('precio', 'Escribe un precio mayor que cero.'); valido = false; }
  return valido;
}

async function guardar(evento) {
  evento.preventDefault();
  if (estado.guardando) return;
  limpiarErrores();

  const datos = {
    nombre: $('f-nombre').value.trim(),
    descripcion: $('f-descripcion').value.trim() || null,
    precio: Number($('f-precio').value),
    categoria: $('f-categoria').value,
    disponible: $('f-disponible').checked
  };
  if (!validarLocal(datos)) {
    const primero = document.querySelector('#form-producto [aria-invalid="true"]');
    if (primero) primero.focus();
    return;
  }

  const edicion = estado.editando;
  estado.guardando = true;
  $('btn-guardar').disabled = true;
  try {
    await pedir(edicion ? API + '/' + edicion.id : API, {
      method: edicion ? 'PUT' : 'POST',
      body: JSON.stringify(datos)
    });
    $('dlg-producto').close();
    avisar(edicion ? 'Cambios guardados' : 'Producto agregado');
    await cargarProductos();
  } catch (e) {
    let mostrado = false;
    if (e.campos) {
      Object.entries(e.campos).forEach(([campo, mensaje]) => { mostrado = mostrarErrorCampo(campo, mensaje) || mostrado; });
    }
    $('err-general').textContent = mostrado ? 'Revisa los campos marcados.' : e.message;
    const primero = document.querySelector('#form-producto [aria-invalid="true"]');
    if (primero) primero.focus();
  } finally {
    estado.guardando = false;
    $('btn-guardar').disabled = false;
  }
}

/* ---------------- Eliminar ---------------- */

function abrirEliminar(producto) {
  estado.porEliminar = producto;
  $('eliminar-texto').textContent = '¿Eliminar «' + producto.nombre + '» de la carta? Esta acción no se puede deshacer.';
  $('dlg-eliminar').showModal();
}

async function confirmarEliminar(evento) {
  evento.preventDefault();
  const producto = estado.porEliminar;
  if (!producto) return;
  $('btn-si-eliminar').disabled = true;
  try {
    await pedir(API + '/' + producto.id, { method: 'DELETE' });
    $('dlg-eliminar').close();
    avisar('Producto eliminado');
  } catch (e) {
    $('dlg-eliminar').close();
    avisar(e.message, true);
  } finally {
    $('btn-si-eliminar').disabled = false;
    estado.porEliminar = null;
  }
  await cargarProductos();
}

/* ---------------- Arranque ---------------- */

function iniciar() {
  $('btn-agregar').addEventListener('click', () => abrirFormulario(null));
  $('btn-cancelar').addEventListener('click', () => $('dlg-producto').close());
  $('form-producto').addEventListener('submit', guardar);
  $('f-descripcion').addEventListener('input', actualizarContador);
  $('solo-disponibles').addEventListener('change', (e) => { estado.soloDisponibles = e.target.checked; renderizar(); });
  $('btn-no-eliminar').addEventListener('click', () => $('dlg-eliminar').close());
  $('form-eliminar').addEventListener('submit', confirmarEliminar);
  renderizarFiltros();
  cargarProductos();
}

document.addEventListener('DOMContentLoaded', iniciar);
