# Social Network Admin Dashboard

## Overview
This document provides information about the admin dashboard features added to the Social Network application. The admin dashboard allows privileged users to manage users, posts, and view statistics about the platform.

## Features
- Admin user management (view, grant/revoke admin privileges, delete)
- Post moderation (view, delete)
- Dashboard with key metrics and statistics
- Secure access control

## Setup
1. **Register an admin account**
   - Register a new account with email `admin@admin.com` through the regular registration process
   - The system will automatically grant admin privileges to this account on startup

2. **Alternative: Make an existing user an admin**
   - Log in as an existing admin
   - Use the admin dashboard to grant admin privileges to other users

## API Endpoints

### Authentication
- Use the standard application authentication system
- All admin endpoints require a valid JWT token and admin privileges

### Admin Dashboard Metrics
```
GET /api/admin/dashboard
```
Returns key metrics including:
- Total users
- Total posts
- Post distribution by month
- Other statistics

### User Management
```
GET /api/admin/users
```
Returns a list of all users in the system

```
PUT /api/admin/users/{userId}/admin-status?isAdmin=true
```
Grant or revoke admin privileges for a specific user

```
DELETE /api/admin/users/{userId}
```
Permanently delete a user account

### Post Moderation
```
GET /api/admin/posts
```
Returns a list of all posts for moderation

```
DELETE /api/admin/posts/{postId}
```
Remove a post that violates community guidelines

## Security Considerations
- Admin access is controlled via a boolean flag on the User entity
- All admin endpoints validate admin privileges before execution
- API endpoints are secured with Spring Security
- JWT tokens are used for authentication

## Development Notes
- Remember to update the `isAdmin` field in the User entity when adding/removing admin privileges
- The system automatically sets up `admin@admin.com` as an admin account if it exists
- Admin endpoints are defined in `AdminController.java` 