import { Injectable } from '@angular/core';

export interface Notification {
  type: string;
  message: string;
  reservationId?: number;
  timestamp: string;
  read?: boolean;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  notifications: Notification[] = [];

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
  }

  addNotification(type: string, message: string, reservationId?: number): void {
    this.notifications.unshift({
      type,
      message,
      reservationId,
      timestamp: new Date().toISOString(),
      read: false
    });
  }

  markAllRead(): void {
    this.notifications.forEach(n => n.read = true);
  }

  markRead(index: number): void {
    if (this.notifications[index]) this.notifications[index].read = true;
  }
}
