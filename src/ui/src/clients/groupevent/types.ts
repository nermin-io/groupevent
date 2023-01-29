export type Organiser = {
  first_name: string;
  last_name: string;
  email_address: string;
};

export enum EventStatus {
  DRAFT = "DRAFT",
  PLANNED = "PLANNED",
  RESCHEDULED = "RESCHEDULED",
  CANCELLED = "CANCELLED",
}

export type Address = {
  id?: string;
  address: string;
  address2?: string;
  city: string;
  state: string;
  post_code: string;
  notes?: string;
  created_at?: string;
  updated_at?: string;
};

export type Event = {
  id?: string;
  name: string;
  description: string;
  organiser?: Organiser;
  address: Address;
  scheduled_date: string;
  time_from: string;
  time_to: string;
  agenda: string;
  status?: EventStatus;
  attendees: Array<string>;
  created_at?: string;
  updated_at?: string;
};

export enum InviteResponse {
  GOING = 'GOING',
  NOT_GOING = 'NOT_GOING'
}

export type Attendee = {
  id?: string;
  first_name?: string;
  last_name?: string;
  email_address: string;
  created_at?: string;
  last_invited?: string;
};

export type Invite = {
  event: Event;
  attendee: Attendee;
  response: string;
  message?: string;
};

export type EventResponse = {
  first_name: string;
  last_name: string;
  response: InviteResponse;
  message: string;
};
