# Restaurante

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 19.2.23.

## Proyecto completo (Angular + Spring Boot)

Inicia el backend en una terminal desde la carpeta `Web`:

```powershell
Set-Location ".\CrustaceoCaribeno-WebProject - copia\demo"
.\mvnw.cmd spring-boot:run
```

Inicia Angular en otra terminal:

```powershell
Set-Location ".\CrustaceoCaribeno-WebProject\Proyecto_Restaurante_Angular"
npm install
npm start
```

La interfaz queda en `http://localhost:4200` y la API en `http://localhost:8080`.

Las porciones disponibles se configuran al crear o editar un plato; si llegan a cero, el menú lo muestra agotado. Al confirmar un pedido, el backend valida y descuenta las porciones; si se cancela antes de entregarlo, las repone. En el perfil, el cliente ve el historial de estados y la página los actualiza cada 10 segundos.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
