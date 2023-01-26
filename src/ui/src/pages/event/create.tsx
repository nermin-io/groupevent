import React from "react";
import { NextPage } from "next";
import Card from "@/components/Card";
import Wizard from "@/containers/Wizard";
import EventNameForm from "@/containers/EventNameForm";
import EventLocationForm from "@/containers/EventLocationForm";
import EventTimeForm from "@/containers/EventTimeForm";
import EventAttendeesForm from "@/containers/EventAttendeesForm";
import { withIronSessionSsr } from "iron-session/next";
import { sessionOptions } from "@/lib/session";
import useAuth from "@/hooks/auth";

const componentsList = [
  EventNameForm,
  EventLocationForm,
  EventTimeForm,
  EventAttendeesForm,
];

const CreateEvent: NextPage = () => {
  const auth = useAuth();

  return (
    <>
      <Card>
        <Wizard components={componentsList} />
      </Card>
    </>
  );
};

export const getServerSideProps = withIronSessionSsr(async function ({
  req,
  res,
}) {
  const { user } = req.session;

  if (!user) {
    return {
      redirect: {
        destination: "/",
        permanent: false,
      },
    };
  }

  return {
    props: {
      session: user,
    },
  };
},
sessionOptions);

export default CreateEvent;
