
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