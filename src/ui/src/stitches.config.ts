import { createStitches} from '@stitches/react';

export const { styled, css, getCssText, globalCss, keyframes } = createStitches({
  media: {
        small: '(max-width: 500px)'
    }
});