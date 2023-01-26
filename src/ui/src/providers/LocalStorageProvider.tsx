import React, { useState, useEffect } from "react";
import LocalStorageContext, { DEFAULT_STATE, StorageState } from "@/context/storage";
import { formatISO } from 'date-fns';

interface Props {
  children: React.ReactNode | React.ReactNode[];
}

const deserializer = (key: string, value: any) => {
  const dateKeys = ['date','timeFrom','timeTo'];
  if(dateKeys.includes(key)) {
    return new Date(value);
  }

  return value;
}

const serializer = (key: string, value: any) => {
  const dateKeys = ['date','timeFrom','timeTo'];
  if(dateKeys.includes(key) && value instanceof Date) {
    return formatISO(value);
  }

  return value;
}

const getInitialState = () => {
  if (typeof window === "undefined") return DEFAULT_STATE;

  const state = localStorage.getItem("state");
  return state ? (JSON.parse(state, deserializer) as StorageState) : DEFAULT_STATE;
};

const LocalStorageProvider: React.FC<Props> = ({ children }) => {
  const [state, setState] = useState(getInitialState);
  const [renderChildren, setRenderChildren] = useState(false);

  useEffect(() => {
    setRenderChildren(true);
  }, []);

  const persist = () => {
    localStorage.setItem("state", JSON.stringify(state, serializer));
  }

  const clear = () => {
    localStorage.setItem("state", JSON.stringify(DEFAULT_STATE, serializer));
    setState(DEFAULT_STATE);
  }

  const setField = (field: keyof StorageState, value: any) => {
    setState(prevState => ({
      ...prevState,
      [field]: value
    }))
  }

  const context = {
    state,
    setState,
    persist,
    clear,
    setField
  };

  return (
    <LocalStorageContext.Provider value={context}>
      {renderChildren && children}
    </LocalStorageContext.Provider>
  );
};

export default LocalStorageProvider;
