package com.ssblur.scriptor.word.descriptor.color

import com.ssblur.scriptor.color.CustomColors.getKey


class CustomColorDescriptor(color: String?) : ColorDescriptor(getKey(color!!))
