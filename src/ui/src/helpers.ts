import Groupevent from "@/clients/groupevent";

export const roundToNearestN = (input: number, n: number) => {
    return Math.round(input / n) * n;
}

export const getCurrentRoundedTime = (): Date => {
  const currentTime = new Date();
  const roundedMinutes = roundToNearestN(currentTime.getMinutes(), 10);

  currentTime.setMinutes(roundedMinutes);
  return currentTime;
};

export const getInitialTimeRange = (): Array<Date> => {
  const timeFrom = getCurrentRoundedTime();
  const timeTo = getCurrentRoundedTime();
  timeTo.setMinutes(timeTo.getMinutes() + 15);

  return [timeFrom, timeTo];
};

export const verifyToken = async (token: string | string[] | undefined) => {
  const decodedToken = decodeURIComponent(`${token}`);

  if (!token)
    return {
      props: {
        error: {
          message: "No access token provided",
        },
      },
    };

  try {
    const response = await Groupevent.post("/event_tokens", { token: decodedToken });

    if (response.status === 200)
      return {
        props: {
          event: response.data as Event,
          token: token
        },
      };

    return {
      props: {
        error: {
          message: response.data.message,
        },
      },
    };
  } catch (err) {
    return {
      props: {
        error: {
          message: "Something went wrong. Please try again later.",
        },
      },
    };
  }
}