package com.github.cc007.poc.atproto.util


/**
 * Takes a handle and returns the social URL for that handle
 * NOTE: This assumes that the first part of the handle is the username and the rest is the social URL
 * In some cases, where the social URL only supports one user, the social URL could be the whole handle.
 * This is not taken into account in this function
 *
 * @receiver the user handle
 * @return the social URL
 */
fun String.toSocialUrl() = split(".", limit = 2).last()