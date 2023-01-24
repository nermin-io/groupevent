import React from "react";
import {getInitialTimeRange} from "../helpers";
import Storage from "../hooks/storage";

export interface StorageState {
  name: string;
  description: string;
  address: string;
  city: string;
  state: string;
  postCode: string;
  notes: string;
  date: Date;
  timeFrom: Date;
  timeTo: Date;
  agenda: string;
  attendees: Array<string>;
}

const [ DEFAULT_TIME_FROM, DEFAULT_TIME_TO ] = getInitialTimeRange();

export const DEFAULT_STATE: StorageState = {
  name: "",
  description: "",
  address: "",
  city: "",
  state: "",
  postCode: "",
  notes: "",
  date: new Date(),
  timeFrom: DEFAULT_TIME_FROM,
  timeTo: DEFAULT_TIME_TO,
  agenda: "",
  attendees: [],
};

export type StorageContext = {
  state: StorageState;
  setState: React.Dispatch<React.SetStateAction<StorageState>>;
  persist: () => void;
  clear: () => void;
  setField: (field: keyof StorageState, value: any) => void;
};

const DEFAULT_CONTEXT_VALUE: StorageContext = {
  state: DEFAULT_STATE,
  setState: () => DEFAULT_STATE,
  persist: () => {},
  clear: () => {},
  setField: () => {}
}

const LocalStorageContext = React.createContext<StorageContext>(DEFAULT_CONTEXT_VALUE);

export default LocalStorageContext;
