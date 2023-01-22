import React from "react";
import { NextPage } from "next";
import Card from "../../components/Card";
import Wizard from "../../containers/Wizard";
import EventNameForm from "../../containers/EventNameForm";
import EventLocationForm from "../../containers/EventLocationForm";
import EventTimeForm from "../../containers/EventTimeForm";

const componentsList = [
    EventNameForm,
    EventLocationForm,
    EventTimeForm
];

const CreateEvent: NextPage = () => {
  return (
    <>
      <Card>
        <Wizard components={componentsList} />
      </Card>
    </>
  );
};

export default CreateEvent;
