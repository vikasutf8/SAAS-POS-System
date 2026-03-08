# SaaS POS System - API Documentation

## Database 
### Docker
```shell
docker exec -it saas-pos-mysql-db mysql -u root -p

root
```

### Schema design

```shell
https://app.eraser.io/workspace/9n9DiRjGqNB2LPjV5IOh?diagram=zr4EsE6GvrSx5F-okKjdI
```

![img.png](img.png)

Base URL: `api/v2`

---

## Table of Contents

- [Auth APIs](#auth-apis)
- [User APIs](#user-apis)
- [Store APIs](#store-apis)
- [Branch APIs](#branch-apis)
- [Product APIs](#product-apis)
- [Category APIs](#category-apis)
- [Inventory APIs](#inventory-apis)
- [Enums](#enums)

---

## Auth APIs

Base Path: `api/v2/auth`

### POST `/signup` - Register a new user

**Request Body** (`UserDto`):
```json
{
  "fullName": "string (required, max 100)",
  "password": "string (required, min 8, max 100)",
  "phone": "string (required, 10-15 digits)",
  "email": "string (required, valid email)",
  "role": "ROLE_ADMIN | ROLE_STORE_MANAGER | ROLE_BRANCH_MANAGER | ROLE_CASHIER | ROLE_CLIENT (required)",
  "storeId": "number (optional)",
  "branchId": "number (optional)"
}
```

**Response** (`AuthResponse`):
```json
{
  "jwt": "string",
  "message": "string",
  "user": {
    "id": 1,
    "fullName": "string",
    "phone": "string",
    "email": "string",
    "storeId": 1,
    "branchId": 1,
    "role": "ROLE_ADMIN",
    "createdAt": "2025-01-01T00:00:00",
    "updatedAt": "2025-01-01T00:00:00",
    "lastLogin": "2025-01-01T00:00:00"
  }
}
```

---

### POST `/login` - Login user

**Request Body** (`UserDto`):
```json
{
  "email": "string (required)",
  "password": "string (required)"
}
```

**Response** (`AuthResponse`):
```json
{
  "jwt": "string",
  "message": "string",
  "user": {
    "id": 1,
    "fullName": "string",
    "email": "string",
    "phone": "string",
    "role": "ROLE_ADMIN",
    "storeId": 1,
    "branchId": 1,
    "createdAt": "2025-01-01T00:00:00",
    "updatedAt": "2025-01-01T00:00:00",
    "lastLogin": "2025-01-01T00:00:00"
  }
}
```

---

## User APIs

Base Path: `api/v2/user/`

### GET `/me` - Get current logged-in user

**Headers**: `Authorization: Bearer <jwt>`

**Response** (`UserDto`):
```json
{
  "id": 1,
  "fullName": "string",
  "phone": "string",
  "email": "string",
  "storeId": 1,
  "branchId": 1,
  "role": "ROLE_ADMIN",
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00",
  "lastLogin": "2025-01-01T00:00:00"
}
```

---

### GET `/token` - Get user from JWT token

**Headers**: `Authorization: Bearer <jwt>`

**Response** (`UserDto`): Same as above.

---

### GET `/{id}` - Get user by ID

**Access**: `ROLE_ADMIN` only

**Path Params**: `id` (Long) - User ID

**Response** (`UserDto`): Same as above.

---

### GET `/email/{email}` - Get user by email

**Access**: `ROLE_ADMIN` only

**Path Params**: `email` (String)

**Response** (`UserDto`): Same as above.

---

### GET `/` - Get all users

**Access**: `ROLE_ADMIN` only

**Response** (`List<UserDto>`):
```json
[
  {
    "id": 1,
    "fullName": "string",
    "phone": "string",
    "email": "string",
    "storeId": 1,
    "branchId": 1,
    "role": "ROLE_ADMIN",
    "createdAt": "2025-01-01T00:00:00",
    "updatedAt": "2025-01-01T00:00:00",
    "lastLogin": "2025-01-01T00:00:00"
  }
]
```

---

## Store APIs

Base Path: `api/v2/store`

### POST `/` - Create store

**Headers**: `Authorization: Bearer <jwt>`

**Request Body** (`StoreDto`):
```json
{
  "branch": "string (required)",
  "brand": "string (required)",
  "storeType": "string (required)",
  "description": "string (optional)",
  "contact": {
    "address": "string",
    "phone": "string",
    "email": "string"
  }
}
```

**Response** (`StoreDto`):
```json
{
  "id": 1,
  "branch": "string",
  "brand": "string",
  "storeType": "string",
  "description": "string",
  "status": "ACTIVE",
  "contact": {
    "address": "string",
    "phone": "string",
    "email": "string"
  },
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

---

### GET `/{id}` - Get store by ID

**Path Params**: `id` (Long) - Store ID

**Response** (`StoreDto`): Same as above.

---

### GET `/` - Get all stores

**Response** (`List<StoreDto>`): Array of StoreDto objects.

---

### GET `/admin` - Get store by current admin

**Headers**: `Authorization: Bearer <jwt>`

**Response** (`Store`): Full Store entity object.

---

### GET `/employee` - Get store by current employee

**Headers**: `Authorization: Bearer <jwt>`

**Response** (`StoreDto`): Same as StoreDto above.

---

### PUT `/{id}` - Update store

**Path Params**: `id` (Long) - Store ID

**Request Body** (`StoreDto`):
```json
{
  "branch": "string",
  "brand": "string",
  "storeType": "string",
  "description": "string",
  "contact": {
    "address": "string",
    "phone": "string",
    "email": "string"
  }
}
```

**Response** (`StoreDto`): Updated store object.

---

### PUT `/{id}/status` - Moderate store status

**Path Params**: `id` (Long) - Store ID

**Query Params**: `storeStatus` - One of: `ACTIVE`, `CLOSED`, `OPEN`, `BLOCKED`, `PENDING`

**Response** (`StoreDto`): Updated store object with new status.

---

### PUT `/{id}` - Soft delete store by ID

**Path Params**: `id` (Long) - Store ID

**Response** (`StoreDto`): Soft-deleted store object.

---

### DELETE `/` - Delete current admin's store

**Headers**: `Authorization: Bearer <jwt>`

**Response**: `void` (200 OK)

---

## Branch APIs

Base Path: `api/v2/branch`

### POST `/` - Create branch

**Headers**: `Authorization: Bearer <jwt>`

**Request Body** (`BranchDto`):
```json
{
  "name": "string",
  "address": "string",
  "phone": "string",
  "email": "string",
  "workingDays": ["MONDAY", "TUESDAY", "WEDNESDAY"],
  "openTime": "09:00:00",
  "closeTime": "21:00:00",
  "storeId": 1
}
```

**Response** (`BranchDto`) - Status `201 CREATED`:
```json
{
  "id": 1,
  "name": "string",
  "address": "string",
  "phone": "string",
  "email": "string",
  "workingDays": ["MONDAY", "TUESDAY"],
  "openTime": "09:00:00",
  "closeTime": "21:00:00",
  "store": {},
  "storeId": 1,
  "manager": {},
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

---

### PUT `/{id}` - Update branch

**Path Params**: `id` (Long) - Branch ID

**Request Body** (`BranchDto`): Same as create request.

**Response** (`BranchDto`): Updated branch object.

---

### DELETE `/{id}` - Delete branch

**Path Params**: `id` (Long) - Branch ID

**Response**: `"Branch deleted successfully"` (200 OK)

---

### GET `/store/{storeId}` - Get all branches by store

**Path Params**: `storeId` (Long)

**Response** (`List<BranchDto>`):
```json
[
  {
    "id": 1,
    "name": "string",
    "address": "string",
    "phone": "string",
    "email": "string",
    "workingDays": ["MONDAY"],
    "openTime": "09:00:00",
    "closeTime": "21:00:00",
    "storeId": 1,
    "createdAt": "2025-01-01T00:00:00",
    "updatedAt": "2025-01-01T00:00:00"
  }
]
```

---

### GET `/{id}` - Get branch by ID

**Path Params**: `id` (Long) - Branch ID

**Response** (`BranchDto`): Single branch object.

---

## Product APIs

Base Path: `api/v2/products`

### POST `/` - Create product

**Headers**: `Authorization: Bearer <jwt>`

**Request Body** (`ProductDto`):
```json
{
  "name": "string",
  "sku": "string",
  "description": "string",
  "mrp": 100.0,
  "sellingPrice": 90.0,
  "brand": "string",
  "imageUri": "string",
  "categoryId": 1,
  "storeId": 1
}
```

**Response** (`ProductDto`):
```json
{
  "id": 1,
  "name": "string",
  "sku": "string",
  "description": "string",
  "mrp": 100.0,
  "sellingPrice": 90.0,
  "brand": "string",
  "imageUri": "string",
  "category": {
    "id": 1,
    "name": "string",
    "storeId": 1
  },
  "storeId": 1,
  "categoryId": 1,
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

---

### PUT `/{id}` - Update product

**Headers**: `Authorization: Bearer <jwt>`

**Path Params**: `id` (Long) - Product ID

**Request Body** (`ProductDto`): Same as create request.

**Response** (`ProductDto`): Updated product object.

---

### DELETE `/{id}` - Delete product

**Headers**: `Authorization: Bearer <jwt>`

**Path Params**: `id` (Long) - Product ID

**Response**: `void` (200 OK)

---

### GET `/store/{storeId}` - Get all products by store

**Path Params**: `storeId` (Long)

**Response** (`List<ProductDto>`): Array of product objects.

---

### GET `/search` - Search products

**Query Params**:
| Param | Type | Description |
|-------|------|-------------|
| `storeId` | Long | Store ID |
| `keyword` | String | Search keyword |

**Response** (`List<ProductDto>`): Array of matching product objects.

---

## Category APIs

Base Path: `api/v2/categories`

### POST `/` - Create category

**Headers**: `Authorization: Bearer <jwt>`

**Request Body** (`CategoryDto`):
```json
{
  "name": "string",
  "storeId": 1
}
```

**Response** (`CategoryDto`):
```json
{
  "id": 1,
  "name": "string",
  "storeId": 1
}
```

---

### GET `/store/{storeId}` - Get categories by store

**Path Params**: `storeId` (Long)

**Response** (`List<CategoryDto>`):
```json
[
  {
    "id": 1,
    "name": "string",
    "storeId": 1
  }
]
```

---

### PUT `/{id}` - Update category

**Headers**: `Authorization: Bearer <jwt>`

**Path Params**: `id` (Long) - Category ID

**Request Body** (`CategoryDto`):
```json
{
  "name": "string",
  "storeId": 1
}
```

**Response** (`CategoryDto`): Updated category object.

---

### DELETE `/{id}` - Delete category

**Headers**: `Authorization: Bearer <jwt>`

**Path Params**: `id` (Long) - Category ID

**Response**: `void` (200 OK)

---

## Inventory APIs

Base Path: `api/v2/inventory`

### POST `/` - Create inventory

**Request Body** (`InventoryDto`):
```json
{
  "branchId": 1,
  "productId": 1,
  "quantity": 100
}
```

**Response** (`InventoryDto`) - Status `201 CREATED`:
```json
{
  "id": 1,
  "branch": {
    "id": 1,
    "name": "string"
  },
  "branchId": 1,
  "product": {
    "id": 1,
    "name": "string"
  },
  "productId": 1,
  "quantity": 100,
  "createdAt": "2025-01-01T00:00:00",
  "lastUpdated": "2025-01-01T00:00:00"
}
```

---

### PUT `/{id}` - Update inventory

**Path Params**: `id` (Long) - Inventory ID

**Request Body** (`InventoryDto`):
```json
{
  "branchId": 1,
  "productId": 1,
  "quantity": 150
}
```

**Response** (`InventoryDto`): Updated inventory object.

---

### DELETE `/{id}` - Delete inventory

**Path Params**: `id` (Long) - Inventory ID

**Response**: `void` (204 No Content)

---

### GET `/{id}` - Get inventory by ID

**Path Params**: `id` (Long) - Inventory ID

**Response** (`InventoryDto`): Single inventory object.

---

### GET `/search` - Get inventory by product and branch

**Query Params**:
| Param | Type | Description |
|-------|------|-------------|
| `productId` | Long | Product ID |
| `branchId` | Long | Branch ID |

**Response** (`InventoryDto`): Matching inventory object.

---

### GET `/branch/{branchId}` - Get all inventory by branch

**Path Params**: `branchId` (Long)

**Response** (`List<InventoryDto>`): Array of inventory objects for the branch.

---

## Enums

### UserRole
| Value | Description |
|-------|-------------|
| `ROLE_ADMIN` | System administrator |
| `ROLE_STORE_MANAGER` | Store manager |
| `ROLE_BRANCH_MANAGER` | Branch manager |
| `ROLE_CASHIER` | Cashier |
| `ROLE_CLIENT` | Client |

### StoreStatus
| Value | Description |
|-------|-------------|
| `ACTIVE` | Store is active |
| `CLOSED` | Store is closed |
| `OPEN` | Store is open |
| `BLOCKED` | Store is blocked |
| `PENDING` | Store is pending approval |
