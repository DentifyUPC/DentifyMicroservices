# Guía de Endpoints - Payment Service

Esta guía muestra cómo usar los endpoints del servicio de pagos en Postman.

## Configuración General

**Para TODOS los endpoints necesitas:**
- **URL Base**: `http://localhost:8080/api/v1/payments` (a través del API Gateway)
- **Header de Autorización**: 
  - Key: `Authorization`
  - Value: `Bearer {TOKEN}` (el token que obtienes al hacer login)

## 1. Actualizar Monto de Pago (Admin)

**ROL REQUERIDO**: `ADMIN`

El administrador actualiza el monto que debe pagar el paciente por una cita.

```
PUT http://localhost:8080/api/v1/payments/{id}/amount

Headers:
- Authorization: Bearer {TOKEN_ADMIN}

Body (JSON):
{
  "amount": 150.00
}
```

**Ejemplo**:
```json
PUT http://localhost:8080/api/v1/payments/1/amount

Body:
{
  "amount": 150.00
}
```

---

## 2. Ver Todos los Pagos (Admin)

**ROL REQUERIDO**: `ADMIN`

El administrador puede ver todos los pagos de todas las citas.

```
GET http://localhost:8080/api/v1/payments

Headers:
- Authorization: Bearer {TOKEN_ADMIN}
```

---

## 3. Ver Pagos de un Paciente (Paciente)

**ROL REQUERIDO**: `PATIENT`

El paciente puede ver todos sus pagos por sus citas.

```
GET http://localhost:8080/api/v1/payments/patient/{patientId}

Headers:
- Authorization: Bearer {TOKEN_PATIENT}
```

**Ejemplo**:
```
GET http://localhost:8080/api/v1/payments/patient/1
```

---

## 4. Ver Pago por ID (Admin o Paciente)

**ROL REQUERIDO**: `ADMIN` o `PATIENT`

```
GET http://localhost:8080/api/v1/payments/{id}

Headers:
- Authorization: Bearer {TOKEN}
```

**Ejemplo**:
```
GET http://localhost:8080/api/v1/payments/1
```

---

## 5. Ver Pago por Appointment ID (Admin o Paciente)

**ROL REQUERIDO**: `ADMIN` o `PATIENT`

```
GET http://localhost:8080/api/v1/payments/appointment/{appointmentId}

Headers:
- Authorization: Bearer {TOKEN}
```

**Ejemplo**:
```
GET http://localhost:8080/api/v1/payments/appointment/5
```

---

## 6. Crear Orden de PayPal (Paciente)

**ROL REQUERIDO**: `PATIENT`

El paciente crea una orden de PayPal para pagar.

```
POST http://localhost:8080/api/v1/payments/paypal/create-order

Headers:
- Authorization: Bearer {TOKEN_PATIENT}

Body (JSON):
{
  "paymentId": 1
}
```

**Respuesta**:
```json
{
  "orderId": "8XJ12345ABC",
  "status": "CREATED",
  "approvalUrl": "https://www.sandbox.paypal.com/checkoutnow?token=8XJ12345ABC"
}
```

**🔴 IMPORTANTE - Cómo aprobar el pago**:
1. Copia la URL completa de `approvalUrl` de la respuesta
2. Ábrela en un **navegador web** (Chrome, Firefox, Edge, etc.)
3. Se abrirá la página de PayPal Sandbox
4. Inicia sesión con tu **cuenta de prueba de PayPal** (no tu cuenta real)
   - Si no tienes cuenta de prueba, créala en: https://developer.paypal.com/dashboard/accounts
5. Aprueba el pago en la página
6. **NO USES EL DASHBOARD DE PAYPAL** - Solo la URL que te devuelve el endpoint

---

## 7. Capturar Pago de PayPal (Paciente)

**ROL REQUERIDO**: `PATIENT`

Después de que el paciente aprueba el pago en PayPal, se captura el pago.

```
POST http://localhost:8080/api/v1/payments/paypal/capture-order

Headers:
- Authorization: Bearer {TOKEN_PATIENT}

Body (JSON):
{
  "paymentId": 1,
  "orderId": "8XJ12345ABC"
}
```

**Respuesta**:
```json
{
  "id": 1,
  "paymentDate": "28/11/2025",
  "amount": 150.00,
  "patientId": 1,
  "state": "PAID",
  "appointmentId": 5,
  "paypalOrderId": "8XJ12345ABC",
  "paypalCaptureId": "9YZ67890DEF"
}
```

---

## Flujo Completo

### Paso 1: Paciente crea una cita
```
POST http://localhost:8080/api/v1/appointment

Headers:
- Authorization: Bearer {TOKEN_PATIENT}

Body:
{
  "patientId": 1,
  "odontologistId": 2,
  "clinicId": 1,
  "shiftName": "Mañana",
  "appointmentDate": "2025-12-01",
  "startTime": "09:00:00",
  "endTime": "10:00:00"
}
```

**→ Se crea automáticamente un pago con estado `PENDING` y monto `0.0`**

---

### Paso 2: Admin actualiza el monto del pago
```
PUT http://localhost:8080/api/v1/payments/1/amount

Headers:
- Authorization: Bearer {TOKEN_ADMIN}

Body:
{
  "amount": 150.00
}
```

---

### Paso 3: Paciente consulta sus pagos
```
GET http://localhost:8080/api/v1/payments/patient/1

Headers:
- Authorization: Bearer {TOKEN_PATIENT}
```

---

### Paso 4: Paciente crea orden de PayPal
```
POST http://localhost:8080/api/v1/payments/paypal/create-order

Headers:
- Authorization: Bearer {TOKEN_PATIENT}

Body:
{
  "paymentId": 1
}
```

---

### Paso 5: Paciente aprueba el pago en PayPal
1. Toma el `approvalUrl` de la respuesta del paso anterior
2. Ábrelo en tu navegador
3. Inicia sesión en PayPal Sandbox con tu cuenta de prueba
4. Aprueba el pago
5. PayPal te redireccionará (ignora la redirección, solo necesitas aprobar)

---

### Paso 6: Paciente captura el pago
```
POST http://localhost:8080/api/v1/payments/paypal/capture-order

Headers:
- Authorization: Bearer {TOKEN_PATIENT}

Body:
{
  "paymentId": 1,
  "orderId": "8XJ12345ABC"
}
```

**→ El estado del pago cambia a `PAID` y se registra la fecha de pago**

---

## Obtener Token de Autenticación

Para obtener el token necesitas hacer login:

```
POST http://localhost:8080/api/v1/authentication/sign-in

Body (JSON):
{
  "username": "tu_usuario",
  "password": "tu_contraseña"
}
```

**Respuesta**:
```json
{
  "id": 1,
  "username": "tu_usuario",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Copia el `token` y úsalo como `Bearer {token}` en el header Authorization de todos los endpoints.

---

## Estados del Pago

- `PENDING`: Pago pendiente, creado automáticamente cuando se crea la cita
- `PAID`: Pago completado a través de PayPal

---

## Notas Importantes

1. **El pago se crea automáticamente** cuando el paciente crea una cita.
2. **Solo el admin** puede actualizar el monto del pago.
3. **Solo el paciente** puede pagar con PayPal.
4. **El admin** puede ver todos los pagos.
5. **El paciente** puede ver solo sus propios pagos.
6. Para todos los endpoints necesitas **URL + Token**, nada más.

---

## ❓ Preguntas Frecuentes

### ¿En qué momento debo ingresar al dashboard de PayPal?
**Nunca**. No necesitas entrar al dashboard de PayPal para hacer pagos. Solo debes:
1. Usar el endpoint `create-order` para crear la orden
2. Abrir la `approvalUrl` que te devuelve
3. Aprobar el pago en esa página
4. Usar el endpoint `capture-order` para capturar el pago

### ¿Qué cuenta de PayPal debo usar?
Debes usar una **cuenta de prueba de PayPal Sandbox**, no tu cuenta real. Puedes crear cuentas de prueba en https://developer.paypal.com/dashboard/accounts

### ¿Por qué me da error "INVALID_PARAMETER_SYNTAX"?
Este error se solucionó. Era un problema de formato de números (350,00 vs 350.00). Ahora está configurado para usar siempre el formato correcto con punto decimal.

### ¿Puedo pagar sin usar PayPal?
No, actualmente solo está implementado el pago con PayPal. El flujo es:
- Cita → Pago PENDING → Admin actualiza monto → Paciente paga con PayPal → Estado PAID

### ¿Cómo integro esto con mi frontend?
Revisa la guía completa en: **FRONTEND_INTEGRATION_GUIDE.md**

Las URLs de retorno ya están configuradas para apuntar al frontend:
- Success: `http://localhost:3000/payment/success`
- Cancel: `http://localhost:3000/payment/cancel`

Solo necesitas crear esas rutas en tu app React/Vue/Angular.
