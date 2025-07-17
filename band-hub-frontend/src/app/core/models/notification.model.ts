export interface Notification {
  id: number;
  type: NotificationType;
  title: string;
  content: string;
  metadata: Record<string, any>;
  createdAt: string;
  read: boolean;
}

export enum NotificationType {
  BAND_INVITATION = "Zaproszenie do zespołu",
  BAND_REQUEST = "Zgłoszenie do zespołu",
  JOINED_BAND = "Dołączono do zespołu",
  BAND_REJECTED_REQUEST = "Odrzucono zgłoszenie",
  NEW_MESSAGE = "Nowa wiadomość",
  NEW_EVENT = "Nowe wydarzenie",
  NEW_BOOKING = "Nowa rezerwacja"
}
