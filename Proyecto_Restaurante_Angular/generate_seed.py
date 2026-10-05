import json
import re
from pathlib import Path

source = Path('demo/src/main/java/com/example/demo/DataLoader.java').read_text(encoding='utf-8')
pattern = re.compile(
    r'productoRepository\.save\(new Producto\(\s*"((?:[^"\\]|\\.)*)",\s*'
    r'([\d.]+),\s*"((?:[^"\\]|\\.)*)",\s*"((?:[^"\\]|\\.)*)"\s*\)\)',
    re.S,
)
products = pattern.findall(source)
if len(products) != 43:
    raise SystemExit(f'Expected 43 seeded products, found {len(products)}')

rows = []
for index, (name, price, description, image) in enumerate(products, start=1):
    category = (
        'Entrada' if index <= 13 else
        'Plato Fuerte' if index <= 29 else
        'Especialidades De La Casa' if index <= 37 else
        'Postre' if index <= 40 else
        'Bebida'
    )
    rows.append({
        'id': index,
        'nombre': name,
        'descripcion': description,
        'precio': float(price),
        'categoria': category,
        'imagenURL': image,
        'activo': True,
    })

seed = json.dumps(rows, ensure_ascii=True, indent=2)
service = f'''import {{ Injectable }} from '@angular/core';
import {{ Comida }} from '../models/comida.model';

@Injectable({{ providedIn: 'root' }})
export class ComidaService {{
  // Datos iniciales del menú de Spring Boot; se conservan en memoria como en el ejemplo del profesor.
  private comidas: Comida[] = {seed};

  // Incluye activos e inactivos para que la tabla de administración pueda reactivarlos.
  getComidas(): Comida[] {{
    return this.comidas;
  }}

  getComidasActivas(): Comida[] {{
    return this.comidas.filter(comida => comida.activo !== false);
  }}

  getComidaById(id: number): Comida | undefined {{
    return this.comidas.find(comida => comida.id === id);
  }}

  addComida(comida: Omit<Comida, 'id'>): void {{
    const nextId = Math.max(0, ...this.comidas.map(item => item.id)) + 1;
    this.comidas.push({{ ...comida, id: nextId, activo: true }});
  }}

  updateComida(id: number, comida: Omit<Comida, 'id'>): void {{
    const index = this.comidas.findIndex(item => item.id === id);
    if (index >= 0) {{
      const activo = this.comidas[index].activo !== false;
      this.comidas[index] = {{ ...this.comidas[index], ...comida, id, activo }};
    }}
  }}

  desactivarComida(id: number): void {{
    const comida = this.getComidaById(id);
    if (comida) comida.activo = false;
  }}

  reactivarComida(id: number): void {{
    const comida = this.getComidaById(id);
    if (comida) comida.activo = true;
  }}

  // Conserva el método que usa la tabla; eliminar equivale a desactivar el producto.
  deleteComida(id: number): void {{
    this.desactivarComida(id);
  }}
}}
'''
target = Path(__file__).resolve().parent / 'src/app/service/comida.service.ts'
old_lines = target.read_text(encoding='utf-8').splitlines()
new_lines = service.splitlines()
lines = [
    '*** Begin Patch',
    '*** Update File: proyecto angular/restaurante/src/app/service/comida.service.ts',
    '@@',
    *('-' + line for line in old_lines),
    *('+' + line for line in new_lines),
    '*** End Patch',
]
print(json.dumps('\n'.join(lines), ensure_ascii=True))
