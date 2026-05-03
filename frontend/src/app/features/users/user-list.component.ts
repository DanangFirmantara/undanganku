import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserListComponent {
  users = [
    {
      id: 1,
      email: 'admin@app.com',
      firstName: 'Admin',
      lastName: 'User',
      isActive: true,
      roles: ['ROLE_ADMIN', 'ROLE_USER'],
    },
    {
      id: 2,
      email: 'user@app.com',
      firstName: 'Test',
      lastName: 'User',
      isActive: true,
      roles: ['ROLE_USER'],
    },
  ];

  onEdit(userId: number) {
    console.log('Edit user:', userId);
  }

  onDelete(userId: number) {
    console.log('Delete user:', userId);
  }

  onAddUser() {
    console.log('Add new user');
  }
}

